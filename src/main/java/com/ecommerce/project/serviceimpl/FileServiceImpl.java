package com.ecommerce.project.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.service.FileService;
@Service
public class FileServiceImpl implements FileService {
	
	public String uploadImage(String path, MultipartFile file) throws IOException {
		// get original file name
		String originalFilename = file.getOriginalFilename();
		//generate random for the files 
		String randomId = UUID.randomUUID().toString();
		 // mat.jpg --> 1234 --> 1234.jpg 
		String fileName = randomId.concat(originalFilename).substring(originalFilename.lastIndexOf('.'));
		//now we have something like this 1122.jpg for this set the path
		String filePath=path+File.separator+fileName;
		
		// Check if path exist and create
        File folder = new File(path);
        if (!folder.exists())
            folder.mkdir();
        
        //upload to the server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        System.out.println("Saving image to: " + filePath);
		return fileName;
	}


}
