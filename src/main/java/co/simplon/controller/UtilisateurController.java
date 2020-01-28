package co.simplon.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.simplon.HttpResponse;
import co.simplon.model.Photo;
import co.simplon.model.Utilisateur;
import co.simplon.repository.PhotoRepository;
import co.simplon.repository.UtilisateurRepository;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin("http://localhost:4200")
public class UtilisateurController {

	@Autowired
	UtilisateurRepository utilisateurRepository;
	
	@Autowired
	PhotoRepository photoRepository;

	@DeleteMapping("/delete")
	public void deleteUser(@RequestParam int id) {
		utilisateurRepository.deleteById(id);
	}

	@GetMapping("/findById")
	public ResponseEntity<?> findById(@RequestParam int id){
		Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
		if(utilisateur.isPresent()) {
			return ResponseEntity.ok(utilisateur.get());
		} else {
			return HttpResponse.NOT_FOUND;
		}
	}

	@GetMapping("/all")
	public @ResponseBody Iterable<Utilisateur> getAllUsers() {
		return utilisateurRepository.findAll();
	}

	@GetMapping("/findByPseudo")
	public ResponseEntity<?> findByPseudo(String pseudo) {
		Optional<Utilisateur> utilisateur = utilisateurRepository.findByPseudo(pseudo);
		if (utilisateur.isPresent()) {
			return ResponseEntity.ok(utilisateur);
		} else {
			return HttpResponse.NOT_FOUND;
		}
	}
	
	@GetMapping("/findByPhoto")
	public ResponseEntity<?> findByPhoto(@RequestParam int id){
		Optional<Photo> photo = photoRepository.findById(id);
		if(photo.isPresent()) {
			Utilisateur utilisateur = photo.get().getUtilisateur();
			return ResponseEntity.ok(utilisateur);
		} else {
			return HttpResponse.NOT_FOUND;
		} 
	}
	
	@PostMapping("/connect")
	public ResponseEntity<String> connection(@RequestBody Utilisateur utilisateur) {
		Optional<Utilisateur> user = utilisateurRepository.findByPseudo(utilisateur.getPseudo());
		if (user.isPresent()) {
			if (utilisateur.getMotDePasse().equals(user.get().getMotDePasse())) {
				return ResponseEntity.ok("connected");
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accès refusé");
			}
		} else {
			return HttpResponse.NOT_FOUND;

		}
	}

}
