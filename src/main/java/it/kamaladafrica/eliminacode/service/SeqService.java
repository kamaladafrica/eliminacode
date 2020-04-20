package it.kamaladafrica.eliminacode.service;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.kamaladafrica.eliminacode.domain.Seq;
import it.kamaladafrica.eliminacode.repository.SeqRepository;

@Service
public class SeqService {

	private final Logger log = LoggerFactory.getLogger(SeqService.class);

	private static final Long INITIAL_VALUE = 1L;
	private static final Long STEP = 1L;

	private final SeqRepository seqRepository;

	public SeqService(SeqRepository seqRepository) {
		this.seqRepository = seqRepository;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public Long nextVal() {
		return compiuteNextVal(buildName());
	}

	private String buildName() {
		LocalDate now = LocalDate.now();
		return now.toString();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public Long nextVal(String name) {
		return compiuteNextVal(name);
	}

	@Transactional(readOnly = true)
	public Optional<Long> previewNextVal() {
		return previewNextVal(buildName());
	}

	@Transactional(readOnly = true)
	public Optional<Long> previewNextVal(String name) {
		return seqRepository.findByName(name)
				.map(Seq::getNextValue);
	}

	private Long compiuteNextVal(String name) {
		Seq seq = seqRepository.findByName(name)
				.orElseGet(() -> getOrCreateSeq(name));

		final Long currentVal = seq.getNextValue();

		seq.setNextValue(seq.getNextValue() + seq.getStep());
		seqRepository.save(seq);
		log.debug("Sequence {}: currentVal = {}, nextval = {}", name, currentVal, seq.getNextValue());
		return currentVal;
	}

	private Seq getOrCreateSeq(String name) {
		Seq newSeq = new Seq();
		newSeq.setName(name);
		newSeq.setNextValue(INITIAL_VALUE);
		newSeq.setStep(STEP);
		return newSeq;
	}

}
