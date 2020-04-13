package it.kamaladafrica.eliminacode.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import it.kamaladafrica.eliminacode.web.rest.TestUtil;

public class SeqTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Seq.class);
        Seq seq1 = new Seq();
        seq1.setId(1L);
        Seq seq2 = new Seq();
        seq2.setId(seq1.getId());
        assertThat(seq1).isEqualTo(seq2);
        seq2.setId(2L);
        assertThat(seq1).isNotEqualTo(seq2);
        seq1.setId(null);
        assertThat(seq1).isNotEqualTo(seq2);
    }
}
