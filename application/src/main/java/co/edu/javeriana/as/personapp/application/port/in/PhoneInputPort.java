package co.edu.javeriana.as.personapp.application.port.in;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import java.util.List;

@Port
public interface PhoneInputPort {

    public void setPersintence(PhoneOutputPort phonePersistence);

    public Phone create(Phone phone);

    public Phone edit(String number, Phone phone) throws NoExistException;

    public Boolean drop(String number) throws NoExistException;

    public List<Phone> findAll();

    public Phone findOne(String number) throws NoExistException;

    public Integer count();

    public Person getOwner(String number) throws NoExistException;
    
}
