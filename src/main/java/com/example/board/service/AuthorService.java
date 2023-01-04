package com.example.board.service;

import com.example.board.domain.Author;
//import com.example.board.repository.JpaRepository;
//import com.example.board.repository.JdbcRepository;
//import com.example.board.repository.JdbcRepository;
//import com.example.board.repository.MemoryRepository;
//import com.example.board.repository.SpringDataJpaRepository;
import com.example.board.repository.AuthorRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorService implements UserDetailsService {

//    외부 접근 불가능
    private final AuthorRepository repository;
    private final PasswordEncoder passwordEncoder;


//    생성자
    public AuthorService(AuthorRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Author> findAll(){
        List<Author> result = repository.findAll();
        return result;
    }

    public void create(Author author){
        repository.save(author);
        author.encodePassword(passwordEncoder);
    }

//    SpringDataJpaRepository일경우
    public Optional<Author> findById(Long memberId){
        return repository.findById(memberId);
    }

    public Optional<Author> findByEmail(String email){
        return repository.findByEmail(email);
    }


//     UserDetailsService은 Spring Security에서 유저의 정보를 가져오는 인터페이스이다.
//    AuthenticationProvider 인터페이스로 유저 정보를 리턴하면, 그 곳에서 사용자가 입력한 정보와 DB에 있는 유저 정보를 비교한다
//    유저의 정보를 UserDetails로 리턴

//    config에서 username을 email로 지정했으므로, 로그인시 email을 username으로 하여 search
//    그렇게 하여 조회해온 User정보를 가지고, 제출한 값과 비교 후 session return

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Author author = repository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 Email 입니다."));
        return new User(author.getEmail(), author.getPassword(), Arrays.asList(new SimpleGrantedAuthority(author.getRole().toString())));
    }
}
