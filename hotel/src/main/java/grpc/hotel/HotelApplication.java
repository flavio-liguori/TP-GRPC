package grpc.hotel;

import models.IdentifiantPassword;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import models.Hotel;
import models.Chambre;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication(scanBasePackages = {
		"services", // Le package contenant HotelServiceImpl
		"models",
})
public class HotelApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);
	}

	@Bean
	public Hotel initializeHotel() throws IOException {
		Hotel hotel = new Hotel(1,"Hôtel Luxe", "123 Rue de Paris", "Paris", "France", 5);

		// Ajout de quelques chambres
		Chambre chambre1 = new Chambre(0, 2, 50, "Chambre pour deux", "/home/flv/Documents/archi/tp3/hotelserveur/src/main/java/rest/hotelserveur/images/image1.jpg");
		Chambre chambre2 = new Chambre(1, 4, 80, "Chambre pour quatre",
				"/home/flv/Documents/archi/tp3/hotelserveur/src/main/java/rest/hotelserveur/images/image1.jpg");
		Chambre chambre3 = new Chambre(2, 4, 60, "Chambre pour quatre - deuxième", "/home/flv/Documents/archi/tp3/hotelserveur/src/main/java/rest/hotelserveur/images/image1.jpg");

		ArrayList<Chambre> chambres = new ArrayList<>();
		IdentifiantPassword id1 = new IdentifiantPassword("id","password");
		IdentifiantPassword id2 = new IdentifiantPassword("id2","password2");
		ArrayList<IdentifiantPassword> ids = new ArrayList<>();
		ids.add(id1);
		ids.add(id2);
		hotel.setIdentifiantPasswords(ids);
		chambres.add(chambre1);
		chambres.add(chambre2);
		chambres.add(chambre3);
		hotel.setChambres(chambres);

		return hotel;
	}
	@Bean
	public ArrayList<IdentifiantPassword> initializeAccount() throws IOException {
		ArrayList<IdentifiantPassword> ids = new ArrayList<>();
		ids.add(new IdentifiantPassword("id","password"));
		ids.add(new IdentifiantPassword("id2","password2"));
		return ids;

	}
}
