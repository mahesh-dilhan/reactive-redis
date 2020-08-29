package com.mopus.tracker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Traker {
    private String trakerId;
    private String messageIdentifer;
    private String subscriptionId;
    private String appId;
    private String deviceId;
    private Payload payload;
    private String status;
    private Date createTimeStamp;
    private Date lastUpdatedTimestamp;


}

@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Payload {
    private Message message;
    private Receiver receiver;
    private MutableData data;
}


@Log4j2
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
class MutableData {
    private String dl;
    private String url;
}

@Log4j2
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
class Message {
    private String title;
    private String body;
    private boolean isMutable;
}
@Builder
@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
class Receiver {
    private String userId;
    private String emailId;
    private String recepientId;
}