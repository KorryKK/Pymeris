package angel.tfg.pymeris.Model.Repository.Clientes;

import angel.tfg.pymeris.Model.DAO.Clientes.ClientesDaoImpl;
import angel.tfg.pymeris.Model.Entities.Cliente;

import java.util.List;

public class ClientesRepositoryImpl implements ClientesRepository {

    public List<Cliente> obtenerTodosLosClientes() {
        return ClientesDaoImpl.obtenerTodos();
    }

    @Override
    public boolean guardarCliente(Cliente cliente) {
        System.out.println("Llamamos al dao para crear");
        return ClientesDaoImpl.guardarCliente(cliente);
    }

    @Override
    public boolean actualizarCliente(Cliente cliente, String dninifanterior) {
        System.out.println("Estamos en el repository.\nLlamamos al dao para actualizar");
        return ClientesDaoImpl.actualizarCliente(cliente, dninifanterior);
    }

    @Override
    public boolean eliminarCliente(String dni) {
        System.out.println("Llamamos al dao para eliminar al cliente");
        return ClientesDaoImpl.eliminarCliente(dni);
    }
}
