package angel.tfg.pymeris.Model.Repository.Clientes;

import angel.tfg.pymeris.Model.Entities.Cliente;

import java.util.List;

public interface ClientesRepository{
    List<Cliente> obtenerTodosLosClientes();
    boolean guardarCliente(Cliente cliente);
    boolean actualizarCliente(Cliente cliente, String dninifanterior);
    boolean eliminarCliente(String dni);
}
