package angel.tfg.pymeris.Views.ManageProducts.CreateProduct;

import angel.tfg.pymeris.Model.Entities.Producto;
import angel.tfg.pymeris.Model.Repository.Productos.ProductosRepository;
import angel.tfg.pymeris.Views.ManageProducts.MaPrContract;
import javafx.application.Platform;

import java.math.BigDecimal;

public class NewProductPresenter implements NewProductContract.Presenter {

    private final NewProductContract.View controller;
    private final ProductosRepository repository;
    private final MaPrContract.Presenter managepresenter;

    public NewProductPresenter(NewProductContract.View controller, ProductosRepository repository, MaPrContract.Presenter managepresenter) {
        this.controller = controller;
        this.repository = repository;
        this.managepresenter = managepresenter;
    }

    @Override
    public void onCrearClick() {
        controller.cargando(true);

        System.out.println("Estamos en el presenter");

        // 1. Obtenemos los textos crudos
        String nombre = controller.getNombre();
        String categoria = controller.getCategoria();
        String descripcion = controller.getDescripcion();
        String precioString = controller.getPrecioUnitario();
        String ivaString = controller.getIVA();

        // 2. Comprobamos si alguno está realmente vacío o en blanco
        if (esNuloOVacio(nombre) || esNuloOVacio(categoria) || esNuloOVacio(descripcion) ||
                esNuloOVacio(precioString) || esNuloOVacio(ivaString)) {

            controller.showErrorCreateProduct("Alguno de los campos está vacío.\nCompruebe los campos.");
            controller.cargando(false);
            return;
        }

        BigDecimal precio;
        int ivaInt;

        try {
            // 3. Procesamos el precio a BigDecimal
            precio = procesarPrecio(precioString);

            // 4. Procesamos el IVA (ahora que sabemos que no es null ni vacío)
            ivaInt = Integer.parseInt(ivaString.trim());

            if (ivaInt < 0 || ivaInt > 100) {
                controller.showErrorCreateProduct("El IVA debe ser un número entre 0 y 100.");
                controller.cargando(false);
                return;
            }

        } catch (NumberFormatException e) {
            // Si el error viene de nuestra validación de decimales específica
            if (e.getMessage().equals("Demasiados decimales")) {
                controller.showErrorCreateProduct("El precio no puede tener más de 2 decimales.");
            } else {
                controller.showErrorCreateProduct("Formato de precio o IVA incorrecto.");
            }
            controller.cargando(false);
            return;
        }

        new Thread(() -> {
            // Si todos los campos están bien
            System.out.println("Todo correcto. Se procede a crear el producto...");
            Producto p = new Producto();
            p.setNombre(nombre);
            p.setCategoria(categoria);
            p.setDescripcion(descripcion);
            p.setPrecioUnitario(precio);
            p.setIVA(ivaInt);

            boolean success = repository.guardarProducto(p);

            if (success) {
                Platform.runLater(() -> {
                    System.out.println("Producto guardado correctamente.");
                    controller.cargando(false);
                    controller.showSuccessCreateProduct("Producto guardado correctamente.");
                    managepresenter.cargarProductos();
                });
            } else {
                Platform.runLater(() -> {
                    controller.showErrorCreateProduct("Producto no guardado correctamente.");
                    controller.cargando(false);
                });
            }
        }).start();
    }

    /**
     * Convierte un String de precio (ej.: "12,50") a un entero en céntimos (1250).
     * Maneja formatos con coma, punto y valida un máximo de 2 decimales.
     */
    private BigDecimal procesarPrecio(String precioString) throws NumberFormatException {
        // 1. Normalizamos: cambiamos coma por punto
        String p = precioString.trim().replace(",", ".");

        // 2. Creamos el BigDecimal.
        // El constructor de String es el más preciso para evitar errores de coma flotante.
        BigDecimal bd = new BigDecimal(p);

        // 3. Validar que no tenga más de 2 decimales visuales
        if (bd.scale() > 2) {
            throw new NumberFormatException("Demasiados decimales");
        }

        // 4. NORMALIZACIÓN: Forzamos que siempre tenga 2 decimales (ej.: "12" -> "12.00")
        // roundingMode.HALF_UP es el redondeo estándar comercial
        return bd.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    // Función auxiliar para limpiar el código de comprobaciones repetitivas
    private boolean esNuloOVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
