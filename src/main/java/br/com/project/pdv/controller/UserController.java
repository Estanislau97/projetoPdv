package br.com.project.pdv.controller;


import br.com.project.pdv.dto.ResponseDTO;
import br.com.project.pdv.dto.UserDTO;
import br.com.project.pdv.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);

    }

    @PostMapping()
    public ResponseEntity post(@Valid @RequestBody UserDTO userDTO) {
        try {
            userDTO.setEnabled(true);
            return new ResponseEntity<>(userService.save(userDTO), HttpStatus.CREATED);

        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping()
    public ResponseEntity put(@Valid @RequestBody UserDTO userDTO) {
        try {
            return new ResponseEntity<>(userService.update(userDTO), HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity<>(new ResponseDTO(error.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>(new ResponseDTO("Usuário removido com sucesso!"), HttpStatus.OK);

        } catch (EmptyResultDataAccessException error) {
            return new ResponseEntity<>(new ResponseDTO("Não foi possível localizar o usuário!"), HttpStatus.BAD_REQUEST);

        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


}
