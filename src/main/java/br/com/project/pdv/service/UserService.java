package br.com.project.pdv.service;

import br.com.project.pdv.dto.UserDTO;
import br.com.project.pdv.entity.UserEntity;
import br.com.project.pdv.exceptions.NoItemException;
import br.com.project.pdv.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private ModelMapper mapper = new ModelMapper();
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(user ->
                new UserDTO(user.getId(), user.getName(), user.isEnabled())).collect(Collectors.toList());
    }

    public UserDTO save(UserDTO userDTO){
        UserEntity userToSave = mapper.map(userDTO, UserEntity.class);
        userRepository.save(userToSave);
        return new UserDTO(userToSave.getId(), userToSave.getName(), userToSave.isEnabled());
    }
    public UserDTO findByid(long id){
        Optional<UserEntity> optional = userRepository.findById(id);

        if(!optional.isPresent()){
            throw new NoItemException("Usuário não encontrado! ");
        }
        UserEntity userEntity = optional.get();
         return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.isEnabled());
    }
    public void  deleteById(long id){
        userRepository.deleteById(id);
    }

    public UserDTO update(UserDTO userDTO){
        UserEntity userToSave = mapper.map(userDTO, UserEntity.class);
        Optional<UserEntity> userToEdit = userRepository.findById(userToSave.getId());

        if(!userToEdit.isPresent()){
            throw new NoItemException("Usuário não encontrado!");
        }
        userRepository.save(userToSave);
        return new UserDTO(userToSave.getId(), userToSave.getName(), userToSave.isEnabled());
    }

}
