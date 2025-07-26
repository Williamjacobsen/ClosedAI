package com.closedai.closedai.chatsystem.history;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatHistoryMapper {

    ChatHistoryDTO toDto(ChatHistoryEntity entity);

    List<ChatHistoryDTO> toDto(List<ChatHistoryEntity> entities);
}
