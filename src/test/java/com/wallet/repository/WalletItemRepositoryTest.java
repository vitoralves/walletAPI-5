package com.wallet.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import javax.swing.JOptionPane;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.util.enums.TypeEnum;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class WalletItemRepositoryTest {

	private static final Date DATE = new Date();
	private static final TypeEnum TYPE = TypeEnum.EN;
	private static final String DESCRIPTION = "CONTA DE LUZ";
	private static final BigDecimal VALUE = BigDecimal.valueOf(65);
	private Long savedWalletItemId = null;
	private Long savedWalletId = null;

	@Autowired
	WalletItemRepository repository;

	@Autowired
	WalletRepository walletRepository;

	@Test
	public void setUp() {
		Wallet w = new Wallet();
		w.setName("Carteira teste");
		w.setValue(BigDecimal.valueOf(250));
		walletRepository.save(w);

		WalletItem wi = new WalletItem();

		wi.setWallet(w);
		wi.setDate(DATE);
		wi.setType(TYPE);
		wi.setDescription(DESCRIPTION);
		wi.setValue(VALUE);

		repository.save(wi);

		savedWalletItemId = wi.getId();
		savedWalletId = w.getId();
	}

	@After
	public void tearDown() {
		repository.deleteAll();
		walletRepository.deleteAll();
	}

	@Test
	public void testSave() {

		Wallet w = new Wallet();
		w.setName("Carteira 1");
		w.setValue(BigDecimal.valueOf(500));

		walletRepository.save(w);

		WalletItem wi = new WalletItem();

		wi.setId(1L);
		wi.setWallet(w);
		wi.setDate(DATE);
		wi.setType(TYPE);
		wi.setDescription(DESCRIPTION);
		wi.setValue(VALUE);

		WalletItem response = repository.save(wi);

		assertNotNull(response);
		assertEquals(response.getDescription(), DESCRIPTION);
		assertEquals(response.getType(), TYPE);
		assertEquals(response.getValue(), VALUE);
		assertEquals(response.getWallet().getId(), w.getId());
	}

	@Test
	public void testUpdate() {
		Optional<WalletItem> wi = repository.findById(2L);
		String description = "Descrição alterada";

		WalletItem changed = wi.get();
		changed.setDescription(description);

		repository.save(changed);

		Optional<WalletItem> newWalletItem = repository.findById(2L);

		assertEquals(description, newWalletItem.get().getDescription());
	}

	@Test
	public void deleteWalletItem() {

		Optional<Wallet> w = walletRepository.findById(2L);
		WalletItem wi = new WalletItem();

		wi.setWallet(w.get());
		wi.setDate(DATE);
		wi.setType(TYPE);
		wi.setDescription(DESCRIPTION);
		wi.setValue(VALUE);

		repository.save(wi);
		repository.deleteById(wi.getId());

		Optional<WalletItem> response = repository.findById(wi.getId());

		assertFalse(response.isPresent());

	}

	@Test
	public void testFindBetweenDates() {
		Optional<Wallet> w = walletRepository.findById(savedWalletId);

		LocalDateTime localDateTime = DATE.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		Date currentDatePlusFiveDays = Date.from(localDateTime.plusDays(5).atZone(ZoneId.systemDefault()).toInstant());
		Date currentDatePlusSevenDays = Date.from(localDateTime.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());

		WalletItem wi = new WalletItem();
		WalletItem w2 = new WalletItem();

		wi.setId(1L);
		wi.setWallet(w.get());
		wi.setDate(currentDatePlusFiveDays);
		wi.setType(TYPE);
		wi.setDescription(DESCRIPTION);
		wi.setValue(VALUE);

		w2.setId(1L);
		w2.setWallet(w.get());
		w2.setDate(currentDatePlusFiveDays);
		w2.setType(TYPE);
		w2.setDescription(DESCRIPTION);
		w2.setValue(VALUE);

		repository.save(wi);
		repository.save(w2);

		//TODO: PageRequest error
		PageRequest pg = new PageRequest(0, 10);
		
		Page<WalletItem> response = repository.findAllByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(
				savedWalletId, DATE, currentDatePlusFiveDays, pg);

		assertEquals(response.getContent().size(), 2);
		assertEquals(response.getTotalElements(), 2);
		assertEquals(response.getContent().get(0).getWallet().getId(), savedWalletId);
	}


}
