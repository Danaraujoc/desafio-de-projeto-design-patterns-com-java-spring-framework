package me.dio.gof.service.impl;

import me.dio.gof.exception.ResourceNotFoundException;
import me.dio.gof.model.Cliente;
import me.dio.gof.model.ClienteRepository;
import me.dio.gof.model.Endereco;
import me.dio.gof.model.EnderecoRepository;
import me.dio.gof.service.ClienteService;
import me.dio.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementação da <b>Strategy</b>{@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 *
 * @author Danaraujoc
 */

@Service
public class ClienteServiceImpl implements ClienteService {

    // Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    // Strategy: Implementar os métodos definidos na interface.
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.orElseThrow(null);
    }

    @Override
    public Cliente inserir(Cliente cliente) {
        salvarClientecomCep(cliente);
        return cliente;
    }

    @Override
    public Cliente atualizar(Long id, Cliente cliente) {
        // Buscar Cliente po ID, caso exista:
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            salvarClientecomCep(cliente);
            return cliente;
        }
        return null;
    }

    @Override
    public void deletar(Long id) {
        // Deletar o Cliente pelo ID.
        clienteRepository.deleteById(id);
    }

    private void salvarClientecomCep(Cliente cliente) {

        // Verificar se o Endereco do Cliente já existe (pelo CEP).
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // Caso não exista, integrar com o ViaCEP e persistir o retorno.
            ResponseEntity<Endereco> response = viaCepService.consultarCep(cep);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Endereco novoEndereco = response.getBody();
                enderecoRepository.save(novoEndereco);
                return novoEndereco;
            } else {
                // Tratar erros ao consultar o ViaCEP.
                throw new ResourceNotFoundException("Erro ao consultar o serviço ViaCEP. Código de status: " + response.getStatusCodeValue());
            }
        });

        if (endereco != null) {
            cliente.setEndereco(endereco);
            try {
                // Inserir Cliente, vinculando o Endereco (novo ou existente).
                clienteRepository.save(cliente);
            } catch (Exception e) {
                // Tratar erros ao salvar o cliente.
                throw new RuntimeException("Erro ao salvar o cliente: " + e.getMessage(), e);
            }
        }
        {
            // Lançar exceção se o endereço não foi encontrado
            throw new ResourceNotFoundException("CEP inválido ou não encontrado: " + cep);
        }
    }
}
