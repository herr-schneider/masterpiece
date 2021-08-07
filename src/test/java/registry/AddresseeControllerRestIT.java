package registry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import registry.dtos.AddresseeDto;
import registry.dtos.CreateAddresseeCommand;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddresseeControllerRestIT {

    @Autowired
    TestRestTemplate template;

    private ModelMapper modelMapper;

    @BeforeEach
    void init() {
        template.delete("/api/addressee/all");

        template.postForObject("/api/addressee",
                new CreateAddresseeCommand("David Doe", "Everywhere"),
                AddresseeDto.class);

        template.postForObject("/api/addressee",
                new CreateAddresseeCommand("John Doe", "Somewhere"),
                AddresseeDto.class);
    }

    @Test
    void TestCreateAddressee() {
        AddresseeDto addresseeDto = template.postForObject("/api/addressee",
                new CreateAddresseeCommand("Somebody Doe", "Everywhere"),
                AddresseeDto.class);

        System.out.println(addresseeDto.getId());

        AddresseeDto aResult = template.exchange(
                "/api/addressee/" + addresseeDto.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AddresseeDto>() {
                }).getBody();


        assertThat(aResult).isEqualTo(addresseeDto);

        List<AddresseeDto> result = template.exchange(
                "/api/addressee",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AddresseeDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(3)
                .extracting(AddresseeDto::getName)
                .containsExactly("David Doe", "John Doe", "Somebody Doe");
    }

    @Test
    void TestUpdate() {
        template.put("/api/addressee/update?name=David Doe",
                new CreateAddresseeCommand("David69", "home"));

        List<AddresseeDto> result = template.exchange(
                "/api/addressee",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AddresseeDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(2);

        assertThat(result.get(0))
                .extracting(AddresseeDto::getName)
                .isEqualTo("David69");
    }

    @Test
    public void testDeletebyName() {
        template.delete("/api/addressee/delByName?name=John Doe");

        List<AddresseeDto> result = template.exchange(
                "/api/addressee",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AddresseeDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(1);
    }


    @Test
    public void testDeepThroat(){
        List<AddresseeDto> result = template.exchange(
                "/api/addressee/deepsearch?name=David",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AddresseeDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(1);

        assertThat(result.get(0))
                .extracting(AddresseeDto::getName)
                .isEqualTo("David Doe");

        result = template.exchange(
                "/api/addressee/deepsearch?address=Everywhere",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AddresseeDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(1);

        assertThat(result.get(0))
                .extracting(AddresseeDto::getAddress)
                .isEqualTo("Everywhere");
    }
}