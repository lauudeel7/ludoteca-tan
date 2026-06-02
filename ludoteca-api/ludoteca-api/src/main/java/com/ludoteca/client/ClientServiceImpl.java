package com.ludoteca.client;

import com.ludoteca.client.model.Client;
import com.ludoteca.client.model.ClientDto;
import com.ludoteca.exception.BadRequestException;
import com.ludoteca.loan.LoanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    LoanRepository loanRepository;

    @Override
    public List<Client> findAll() {
        return (List<Client>) this.clientRepository.findAll();
    }

    @Override
    public void save(Long id, ClientDto dto) throws BadRequestException {
        Client client;

        if (id == null) {

            if (this.clientRepository.existsByName(dto.getName())) {
                throw new BadRequestException("Ya existe un cliente registrado con el nombre: " + dto.getName());
            }
            client = new Client();
        } else {
            client = this.clientRepository.findById(id).orElse(null);
        }

        if (!client.getName().equalsIgnoreCase(dto.getName()) && this.clientRepository.existsByName(dto.getName())) {
            throw new BadRequestException("No puedes usar ese nombre porque ya pertenece a otro cliente.");
        }

        client.setName(dto.getName());

        this.clientRepository.save(client);
    }

    @Override
    public void delete(Long id) throws BadRequestException {
        if (!this.clientRepository.existsById(id)) {
            throw new BadRequestException("El cliente seleccionado no existe o ya ha sido eliminado.");
        }

        if (this.loanRepository.existsByClientId(id)) {
            throw new BadRequestException("No se puede eliminar el cliente porque tiene préstamos asociados.");
        }

        this.clientRepository.deleteById(id);
    }

    @Override
    public Client get(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }
}
