package ru.shift.userimporter.core.util;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.hash.Funnels;
import com.google.common.io.ByteStreams;
import com.google.common.hash.Hashing;
import com.google.common.hash.Hasher;

public class MultipartFileUtils{

	public static String hashMultipartFile(MultipartFile file) throws IOException{

		try (InputStream inputStream = file.getInputStream()){
			Hasher hasher = Hashing.murmur3_128().newHasher();
			ByteStreams.copy(inputStream, Funnels.asOutputStream(hasher));
			return hasher.hash().toString();
		}

	}
	
}
