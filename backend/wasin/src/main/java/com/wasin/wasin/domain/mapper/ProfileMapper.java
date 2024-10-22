package com.wasin.wasin.domain.mapper;

import com.wasin.wasin.domain.dto.ProfileDTO;
import com.wasin.wasin.domain.dto.ProfileResponse;
import com.wasin.wasin.domain.entity.Profile;
import com.wasin.wasin.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProfileMapper {

    public ProfileResponse.FindAll profileToFindAllDTO(List<Profile> profileList, User user) {
        Profile profile = user.getCompany().getProfile();
        Long profileId = (profile == null) ? -1L : profile.getId();
        return new ProfileResponse.FindAll(
                user.getCompany().getIsAuto(),
                profileId,
                profileList.stream().map(it -> new ProfileResponse.ProfileEachDTO(
                        it.getId(),
                        it.getTitle(),
                        it.getDescription(),
                        it.getTip()
                )).toList()
        );
    }

    public List<Profile> dtoListToEntityList(List<ProfileDTO> profileList) {
        return profileList.stream()
                .map(profileDTO ->
                    Profile.builder()
                        .index(profileDTO.getIndex())
                        .title(profileDTO.getTitle())
                        .description(profileDTO.getDescription())
                        .tip(profileDTO.getTip())
                        .ssh(profileDTO.getSsh())
                        .build()
                ).toList();
    }

    public List<ProfileDTO> entityListToDtoList(List<Profile> profileList) {
        return profileList.stream()
                .map(profile ->
                    new ProfileDTO(
                            profile.getIndex(),
                            profile.getTitle(),
                            profile.getDescription(),
                            profile.getTip(),
                            profile.getSsh()
                    )
                ).toList();
    }

}
