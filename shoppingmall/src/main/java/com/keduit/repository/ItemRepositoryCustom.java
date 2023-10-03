package com.keduit.repository;

import com.keduit.dto.ItemSearchDto;
import com.keduit.dto.MainItemDto;
import com.keduit.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto,
                                      Pageable pageable);

}
