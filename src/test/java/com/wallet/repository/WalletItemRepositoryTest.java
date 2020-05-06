package com.wallet.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class WalletItemRepositoryTest {

	private static final Date DATE = new Date();
	private static final String TYPE = "EN";
	private static final String DESCRIPTION = "CONTA DE LUZ";
	private static final BigDecimal VALUE = BigDecimal.valueOf(65);

	@Autowired
	WalletItemRepository repository;

	@Autowired
	WalletRepository walletRepository;

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

}
