package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Cake;
import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.repository.BuyRepository;
import com.mycompany.myapp.service.dto.BuyingDTO;
import com.mycompany.myapp.service.dto.CakeDTO;
import com.mycompany.myapp.service.mapper.CakeMapper;
import com.mycompany.myapp.service.mapper.CustomerMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BuyService {
    private final BuyRepository buyRepository;
    private final CustomerMapper customerMapper;
    private final CakeMapper cakeMapper;

    public BuyService(BuyRepository buyRepository, CustomerMapper customerMapper, CakeMapper cakeMapper) {
        this.buyRepository = buyRepository;
        this.customerMapper = customerMapper;
        this.cakeMapper = cakeMapper;
    }

    public Page<BuyingDTO> getAllBuying(Pageable pageable) {
        Page<Object[]> buyPage = buyRepository.findBuying(pageable);
        List<Object[]> buyObjectList = buyPage.getContent();
        List<BuyingDTO> buyingList = new ArrayList<>();
        int currentIndex = 0;
        while (currentIndex < buyObjectList.size()) {
            Customer currentCustomer = (Customer) buyObjectList.get(currentIndex)[0];
            BuyingDTO buyingDTO = new BuyingDTO();
            buyingDTO.setCustomerDTO(customerMapper.toDto(currentCustomer));
            List<CakeDTO> cakeList = new ArrayList<>();
            cakeList.add(cakeMapper.toDto((Cake) buyObjectList.get(currentIndex)[1]));
            currentIndex++;
            while (currentIndex < buyObjectList.size()) {
                Customer nextCustomer = (Customer) buyObjectList.get(currentIndex)[0];
                if (currentCustomer.getId().equals(nextCustomer.getId())) {
                    cakeList.add(cakeMapper.toDto((Cake) buyObjectList.get(currentIndex)[1]));
                    currentIndex++;
                } else {
                    break;
                }
            }
            buyingDTO.setCakeDTOList(cakeList.toArray(new CakeDTO[0]));
            buyingList.add(buyingDTO);
        }
        return new PageImpl<>(buyingList, pageable, buyPage.getTotalElements());
    }
}
