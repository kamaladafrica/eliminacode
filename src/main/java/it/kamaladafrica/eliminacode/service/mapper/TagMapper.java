package it.kamaladafrica.eliminacode.service.mapper;

import org.mapstruct.Mapper;

import it.kamaladafrica.eliminacode.domain.Tag;
import it.kamaladafrica.eliminacode.service.dto.TagDTO;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TagMapper extends EntityMapper<TagDTO, Tag> {

	default Tag fromId(Long id) {
		if (id == null) {
			return null;
		}
		Tag tag = new Tag();
		tag.setId(id);
		return tag;
	}
}
