package registry.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import registry.dtos.ArrivalCommand;
import registry.dtos.PostPackageDto;
import registry.entities.Addressee;
import registry.entities.PostPackage;
import registry.repos.AddresseeRepo;
import registry.repos.PostPackageRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostPackageService {

    private PostPackageRepo postPackageRepo;

    private ModelMapper modelMapper;

    private AddresseeRepo addresseeRepo;

    public PostPackageService(PostPackageRepo postPackageRepo, ModelMapper modelMapper, AddresseeRepo addresseeRepo) {
        this.postPackageRepo = postPackageRepo;
        this.modelMapper = modelMapper;
        this.addresseeRepo = addresseeRepo;
    }

    public List<PostPackageDto> listAll() {
        return postPackageRepo.findAll()
                .stream()
                .map(a -> modelMapper.map(a, PostPackageDto.class))
                .collect(Collectors.toList());
    }

    public PostPackageDto createPackage(ArrivalCommand command) {
        PostPackage postPackage = new PostPackage(command.getDoku(), command.getHU_num(),
                command.getArrival(), command.getSender(), command.getAddressee(), command.getStorageStatus());
        addresseeRepo.save(command.getAddressee());
        postPackageRepo.save(postPackage);
        return modelMapper.map(postPackage, PostPackageDto.class);
    }

    @Transactional
    public PostPackageDto updateWithDoku(Optional<String> dokuNum, ArrivalCommand command) throws IllegalArgumentException {
//        String doku = numOfPack.orElseThrow(() -> new IllegalArgumentException("Doku number invalid!"));
        String dokuN;
        if (dokuNum.isPresent()){dokuN = dokuNum.get();}
        else {throw new IllegalArgumentException("Invalid Doku number");}
        PostPackage postPackage = postPackageRepo.findByDoku(dokuN).orElseThrow(()
                -> new IllegalArgumentException("Doku number not found!"));
        Addressee addressee = command.getAddressee();
        postPackage.setDoku(command.getDoku());
        postPackage.setHU_num(command.getHU_num());
        postPackage.setArrival(command.getArrival());
        postPackage.setSender(command.getSender());
        addresseeRepo.save(addressee);
        postPackage.setAddressee(addressee);
        postPackage.setStorageStatus(command.getStorageStatus());

        return modelMapper.map(postPackage, PostPackageDto.class);
    }

    public void delAll() {
        postPackageRepo.deleteAll();
    }

    public PostPackageDto findByDoku(Optional<String> dokuNum){
        String dokuN;
        if (dokuNum.isPresent()){dokuN = dokuNum.get();}
        else {throw new IllegalArgumentException("Invalid Doku number");}
        PostPackage postPackage = postPackageRepo.findByDoku(dokuN).orElseThrow(()
                -> new IllegalArgumentException("Doku number not found!"));
        return modelMapper.map(postPackage, PostPackageDto.class);
    }
}
