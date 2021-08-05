package registry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import registry.dtos.AddresseeDto;
import registry.dtos.ArrivalCommand;
import registry.dtos.PostPackageDto;
import registry.entities.Addressee;
import registry.entities.Sender;
import registry.entities.StorageStatus;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostPackageControllerRestIT {

    @Autowired
    TestRestTemplate template;

    @BeforeEach
    void init() {
        template.delete("/api/registry/all");

        template.postForObject("/api/registry/packages",
                new ArrivalCommand("0123456789", "1234", LocalDate.now(),
                        new Sender("John Doe", "Neverland"),
                        new Addressee("Jane Doe", "Everywhere"),
                        StorageStatus.SEIZED), PostPackageDto.class);

        template.postForObject("/api/registry/packages",
                new ArrivalCommand("1234567890", "4321", LocalDate.now(),
                        new Sender("Steve Doe", "Neverland"),
                        new Addressee("Somebody Doe", "Everywhere"),
                        StorageStatus.SEIZED), PostPackageDto.class);
    }

    @Test
    void TestCreatePackage() {
        PostPackageDto postPackage = template.postForObject("/api/registry/packages",
                new ArrivalCommand("5555555555", "1111", LocalDate.now(),
                        new Sender("Josef Doe", "Russia"),
                        new Addressee("Somebody Doe", "Everywhere"),
                        StorageStatus.SEIZED), PostPackageDto.class);

        PostPackageDto aResult = template.exchange(
                "/api/registry/packages/search?dokuNumber=5555555555",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PostPackageDto>() {
                }).getBody();

        assertThat(aResult).isEqualTo(postPackage);

        assertThat(postPackage)
                .extracting(PostPackageDto::getDoku)
                .isEqualTo("5555555555");


        List<PostPackageDto> result = template.exchange(
                "/api/registry/packages",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PostPackageDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(3)
                .extracting(PostPackageDto::getDoku)
                .containsExactly("0123456789", "1234567890", "5555555555");
    }

    @Test
    void TestRead() {
        List<PostPackageDto> result = template.exchange(
                "/api/registry/packages",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PostPackageDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(2)
                .extracting(PostPackageDto::getDoku)
                .containsExactly("0123456789", "1234567890");
    }

    @Test
    void TestListEmployees() {
        template.put("/api/registry/packages?dokuNumber=0123456789",
                new ArrivalCommand("5555555555", "1111", LocalDate.of(2022, 01, 01),
                        new Sender("Josef Doe", "Russia"),
                        new Addressee("Somebody Doe", "Everywhere"),
                        StorageStatus.SHIPPED));

        List<PostPackageDto> result = template.exchange(
                "/api/registry/packages",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PostPackageDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(2);

        assertThat(result.get(0))
                .extracting(PostPackageDto::getDoku)
                .isEqualTo("5555555555");
    }

    @Test
    public void testFindbyDoku() {
        PostPackageDto result = template.exchange(
                "/api/registry/packages/search?dokuNumber=1234567890",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PostPackageDto>() {
                }).getBody();
    }
}

