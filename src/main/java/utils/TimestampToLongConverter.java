package utils;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;

public class TimestampToLongConverter implements AttributeConverter<Long, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(Long attribute) {
		if (attribute == -1L) return null;
		return new Timestamp(attribute);
	}

	@Override
	public Long convertToEntityAttribute(Timestamp dbData) {
		if (dbData == null) return -1L;
		return dbData.getTime();
	}

}
