package registry.services;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import registry.dtos.AddresseeDto;
import registry.dtos.CreateAddresseeCommand;
import registry.dtos.PostPackageDto;
import registry.entities.Addressee;
import registry.repos.AddresseeRepo;
import registry.validators.Violation;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public AddresseeDto createAddressee(CreateAddresseeCommand command) {
        Addressee addressee = new Addressee(command.getName(), command.getAddress());
        addresseeRepo.save(addressee);
        return modelMapper.map(addressee, AddresseeDto.class);
    }

    @Transactional
    public AddresseeDto updateWithName(Optional<String> name, CreateAddresseeCommand command) {
        if (!name.isPresent()){throw new IllegalStateException("Invalid addressee name!");}
        Addressee addressee = addresseeRepo.findByName(name)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Addressee with name" + name.get() + "not found!"));
            addressee.setName(command.getName());
            addressee.setAddress(command.getAddress());
            return modelMapper.map(addressee, AddresseeDto.class);
    }

    public AddresseeDto delById(long id) {
        Addressee addressee = addresseeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Addressee with ID" + id + "not found!"));
        addresseeRepo.deleteById(id);
        return modelMapper.map(addressee, AddresseeDto.class);
    }

    public AddresseeDto updateWithId(long id, CreateAddresseeCommand command) {
        Addressee addressee = addresseeRepo.findById(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Addressee with ID" + id + "not found!"));
        addressee.setName(command.getName());
        addressee.setAddress(command.getAddress());
        return modelMapper.map(addresseeRepo.findById(addressee.getId()), AddresseeDto.class);
    }

    public AddresseeDto getAddressee(long id) {
        Addressee addressee = addresseeRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Addressee with ID " + id + " not found!"));
        return modelMapper.map(addressee, AddresseeDto.class);
    }

    public List<AddresseeDto> getAddresseeFiltered(Optional<String> name, Optional<String> address) {
        List<AddresseeDto> result = addresseeRepo.findAll()
                .stream()
                .filter(n -> name.isEmpty() || n.getName().contains(name.get()))
                .filter(a -> address.isEmpty() || a.getAddress().contains(address.get()))
                .map(a -> modelMapper.map(a, AddresseeDto.class))
                .collect(Collectors.toList());

        // Type targetType = new TypeToken<List<EmployeeDto>>() {}.getType();

        return result;
    }

    public List<AddresseeDto> getAddresseeByName(Optional <String> name) {
        Optional<String> partName = Stream.of(Optional.of("°%"), name, Optional.of("°%"))
                .flatMap(Optional::stream).reduce(String::concat);
        List<AddresseeDto> addressees = addresseeRepo.findByName(partName)
                .stream()
                .map(a -> modelMapper.map(a, AddresseeDto.class))
                .collect(Collectors.toList());
        return addressees;
    }
}
