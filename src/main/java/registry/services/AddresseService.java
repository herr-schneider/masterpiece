package registry.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import registry.dtos.AddresseeDto;
import registry.entities.Addressee;
import registry.repos.AddresseeRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddresseService {

    private AddresseeRepo addresseeRepo;

    private ModelMapper modelMapper;

    public AddresseService(AddresseeRepo addresseeRepo, ModelMapper modelMapper) {
        this.addresseeRepo = addresseeRepo;
        this.modelMapper = modelMapper;
    }

    public List<AddresseeDto> listAll() {
        return addresseeRepo.findAll()
                .stream()
                .map(a -> modelMapper.map(a, AddresseeDto.class))
                .collect(Collectors.toList());
    }

    public void delAll() {
        addresseeRepo.deleteAll();
    }

    public AddresseeDto delByName(Optional<String> name) {
        Addressee addressee = addresseeRepo.findByName(name).stream()
                .findFirst()
                .orElseThrow( ()-> new IllegalArgumentException("vagy sok vagy keves van"));
        addresseeRepo.delete(addressee);
        return modelMapper.map(addressee, AddresseeDto.class);
    }
}
