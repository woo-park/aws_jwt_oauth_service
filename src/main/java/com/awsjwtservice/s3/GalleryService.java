package com.awsjwtservice.s3;


import com.awsjwtservice.domain.Gallery;
import com.awsjwtservice.dto.GalleryDto;
import com.awsjwtservice.repository.GalleryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GalleryService {
    private GalleryRepository galleryRepository;

    public void savePost(GalleryDto galleryDto) {
        galleryRepository.save(galleryDto.toEntity());
    }

    public List<Gallery> getAllFiles() {
        return galleryRepository.findAllByDelCheck(0);
    }

    public Optional<Gallery> getFile(Long fileSeq) { return galleryRepository.findById(fileSeq);}

    public void deleteFile(Long gallerySeq) {
        Optional<Gallery> file = galleryRepository.findById(gallerySeq);
        if (file.isPresent()) {
            file.get().setDelCheck(1);
        }
    }
}