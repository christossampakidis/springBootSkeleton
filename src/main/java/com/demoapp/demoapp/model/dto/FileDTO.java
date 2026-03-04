package com.demoapp.demoapp.model.dto;

public record FileDTO(byte[] content, String contentType, String fileName) {

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof FileDTO(byte[] otherContent, String otherContentType, String otherFileName)))
			return false;
		return java.util.Arrays.equals(content, otherContent)
				&& java.util.Objects.equals(contentType, otherContentType)
				&& java.util.Objects.equals(fileName, otherFileName);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(java.util.Arrays.hashCode(content),
				contentType, fileName);
	}

	@Override
	public String toString() {
		return "FileDTO{" + "content=" + java.util.Arrays.toString(content)
				+ ", contentType='" + contentType + '\'' + ", fileName='"
				+ fileName + '\'' + '}';
	}
}
