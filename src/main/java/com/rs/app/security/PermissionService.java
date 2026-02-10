package com.rs.app.security;

import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.Authorable;
import com.rs.app.dto.blognews.BlogNewsDTO;
import com.rs.app.dto.comment.CommentDTO;
import com.rs.app.dto.community.CommunityDTO;
import com.rs.app.services.BlogNewsCommentService;
import com.rs.app.services.BlogNewsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("permissionService")
public class PermissionService {
    private final BlogNewsService blogNewsService;
    private final BlogNewsCommentService blogNewsCommentService;

    public PermissionService(BlogNewsService blogNewsService, BlogNewsCommentService blogNewsCommentService) {
        this.blogNewsService = blogNewsService;
        this.blogNewsCommentService = blogNewsCommentService;
    }

    public boolean hasAccessToPublication(Authentication authentication, PublicationStatus status, String requiredAuthority){

        if(status == PublicationStatus.PUBLISHED){
            return true;
        }

        if (!authentication.isAuthenticated()){
            return false;
        }

        return authentication.getAuthorities().stream().anyMatch(
                a -> Objects.equals(a.getAuthority(), requiredAuthority));
    }

    public boolean isAuthorOrMod(Authentication authentication, Authorable authorableDTO){
        if (hasAccessToPublication(authentication, authorableDTO.status(), "MODERATOR")){
            return true;
        }

        if (authentication.getPrincipal() instanceof OAuth2User auth2User){
            Long authorId = authorableDTO.authorId();
            Long userId = auth2User.getAttribute("userId");
            return Objects.equals(authorId, userId);
        }
        return false;
    }

    public boolean hasAccessToBlogNews(Authentication authentication, BlogNewsDTO blogNews){
        if (hasAccessToPublication(authentication, blogNews.status(), "MODERATOR")){
            return true;
        }

        if (authentication.getPrincipal() instanceof OAuth2User auth2User){
            Long authorId = blogNews.author().id();
            Long userId = auth2User.getAttribute("userId");
            return Objects.equals(authorId, userId);
        }
        return false;
    }

    public boolean hasAccessToCommunity(Authentication authentication, CommunityDTO communityDTO){
        if (hasAccessToPublication(authentication, communityDTO.status(), "MODERATOR")){
            return true;
        }

        if (authentication.getPrincipal() instanceof OAuth2User auth2User){
            Long authorId = communityDTO.author().id();
            Long userId = auth2User.getAttribute("userId");
            return Objects.equals(authorId, userId);
        }
        return false;
    }

    public boolean isCommunityOwner(Authentication authentication, CommunityDTO communityDTO){
        if (hasAccessToPublication(authentication, communityDTO.status(), "MODERATOR")){
            return true;
        }

        Long ownerId = communityDTO.owner().id();
        if (authentication.getPrincipal() instanceof OAuth2User auth2User) {
            Long userId = auth2User.getAttribute("userId");
            return Objects.equals(ownerId, userId);
        }
        return false;
    }

    public boolean canUpdateBlog(Authentication authentication, Long id){
        if(authentication.getPrincipal() instanceof OAuth2User auth2User){
            BlogNewsDTO blogNewsDTO = blogNewsService.getOne(id);
            Long authorId = blogNewsDTO.author().id();
            Long userId = auth2User.getAttribute("userId");
            return Objects.equals(authorId, userId);
        }
        return false;
    }

    public boolean isCommentOwner(Authentication authentication, Long id){
        if(authentication.getPrincipal() instanceof  OAuth2User auth2User){
            CommentDTO commentDTO = blogNewsCommentService.getOne(id);
            Long authorId = commentDTO.author().id();
            Long userId = auth2User.getAttribute("userId");
            return Objects.equals(authorId, userId);
        }
        return false;
    }

}
