package uk.co.landbit.twittertofeed.rome;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtomWriterTest {

    private static final Logger LOG = LoggerFactory.getLogger(AtomWriterTest.class);

//    @Test
//    public void shouldConvertHTMLtoReadabilityLikeContent() throws Exception {
//
//	int nbPerPage = 30;
//	int max = 300;
//
//	for (int i = 0; i < max; i++) {
//	    LOG.debug("i {} page {}", i, (i / nbPerPage));
//	}
//
//    }

    @Test
    public void shouldPaginate() throws Exception {

	Integer nbPerPage = 30;
	Integer max = 300;

	for (int i = 0; i < max; i++) {
	    Integer j = i;
	    Integer k = j/nbPerPage;
	    LOG.debug("i {} page {}", i, k);
	}

    }

}
