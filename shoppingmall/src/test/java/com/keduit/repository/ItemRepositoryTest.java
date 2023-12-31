package com.keduit.repository;

import com.keduit.constant.ItemSellStatus;
import com.keduit.entity.Item;
import com.keduit.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.stream.Location;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(15000);
        item.setItemDetail("테스트 상품 상세 정보");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(10000);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        //쿼리문 작성 하기
        Item savedItem = itemRepository.save(item);

        System.out.println("savedItem = " + savedItem);
    }

    public void createItemList(){
        for (int i=1; i<=20; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(15000+i);
            item.setItemDetail("테스트 상품 상세 정보"+i);
            if (i < 11){
                item.setItemSellStatus(ItemSellStatus.SELL);
                item.setStockNumber(10000);
            } else{
                item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
                item.setStockNumber(0);
            }
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            //쿼리문 작성 하기
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : itemList){
            System.out.println("item = " + item);
        }
    }

    @Test
    public void findByItemNmOrItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1","테스트 상품 상세 정보3");
        for (Item item : itemList){
            System.out.println("item = " + item);
        }
    }

    @Test
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(15005);
        for (Item item : itemList){
            System.out.println("item = " + item);
        }
    }

    @Test
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList();
        List<Item> items = itemRepository.findByPriceLessThanOrderByPriceDesc(15008);
        for (Item item : items){
            System.out.println("item = " + item);
        }
    }


    @Test
    @DisplayName("@Query를 사용한 테스트")
    public void findByItemDetail() {
//        this.createItemList();
        Pageable pageable = PageRequest.of(0,15, Sort.by("price").descending());
//        Page<Item> result = itemRepository.findAll(pageable);
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 정보",pageable);
        for (Item item : itemList){
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailByNative() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품");
        for (Item item : itemList){
            System.out.println("item = " + item);
        }
    }

    @Test
    public void testSelect(){
        Long id = 10L;
        this.createItemList();
        Optional<Item> result = itemRepository.findById(id);
        System.out.println("================");
        if (result.isPresent()){
            Item item = result.get();
            System.out.println("item = " + item);
        }
    }
    
    @Test
    @Transactional
    public void testSelect2(){
        Long id = 25L;
        Item item = itemRepository.getOne(id);
        System.out.println("===============");
        System.out.println("item = " + item);
    }

    @Test
    public void testUpdate(){
        Item item = Item.builder().id(25L).itemNm("상품명 수정이오~")
                .itemDetail("상세 정보 체인지~").price(25000).build();
        itemRepository.save(item);
        System.out.println("item = " + item);
    }

    @Test
    public void testDelete(){
        Long id = 25L;
        itemRepository.deleteById(id);
    }
    
    @Test
    public void testPageDefault(){
        Pageable pageable = PageRequest.of(0,10);
        Page<Item> result = itemRepository.findAll(pageable);
        System.out.println("result = " + result);
//        log.info(result.toString());
    }

    //paging & sort
    @Test
    public void testSort(){
        Sort sort1 = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(1,20, sort1);
        Page<Item> result = itemRepository.findAll(pageable);
        result.get().forEach(item -> {
            System.out.println("item = " + item);
        });
    }

    @Test
    @DisplayName("QueryDsl 조회 테스트")
    public void queryDslTest(){
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        //생성
        QItem qItem = QItem.item;
        //sql문 작성하듯이 만든다.
        List<Item> itemList = queryFactory.select(qItem).from(qItem).where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"테스트"+"%"))
                .orderBy(qItem.price.desc()).fetch();
        
        for (Item item : itemList){
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("queryDsl 테스트 2번")
    public void queryDslTest2(){
        BooleanBuilder builder = new BooleanBuilder();
        QItem qitem = QItem.item;
        String itemDetail = "테스트";
        int price = 15005;
        String itemSellStatus = "SELL";

        builder.and(qitem.itemDetail.like("%"+itemDetail+"%"));
        builder.and(qitem.price.gt(price));

        if (StringUtils.equals(itemSellStatus, ItemSellStatus.SELL)){
            builder.and(qitem.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);
        Page<Item> result = itemRepository.findAll(builder, pageable);

        System.out.println("전체 페이지 수 = " + result.getTotalPages());
        System.out.println("조회 한 전체 상품 수 = " + result.getTotalElements());
        System.out.println("현재 페이지 번호(number) = " + result.getNumber());
        System.out.println("페이지 별 게시물 수(size) = " + result.getSize());
        for (Item item : result.getContent()){
            System.out.println("item = " + item);
        }
    }
}