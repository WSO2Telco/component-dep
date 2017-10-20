# Notify Event Mediator

The Notify Event Mediator  constructs events and publishes them to different systems such as DAS or CEP. This is done via event sinks.

Compared to in-built Publish Event Mediator, Notify Event Mediator does not wait until event get insert in to the data-bridge queue.

Events which fails at data-bridge queue can be preserved through a logger.

```
The Notify Event mediator is a content-aware mediator.
```

## Configuring Mediator
Build this repository with maven

Add following .jar files to *CARBON_HOME/repository/components/dropins*

* notifyevent-mediator-<version>.jar

## Configuring Logging
Following configuration is required to dump failing events to a desired location.

Add following segment to the end of file *CARBON_HOME/repository/conf/log4j.properties*

```
# Enable logging for notifyeventmediator package, but not for FaultEventHandler class
log4j.logger.org.wso2telco.dep.notifyeventmediator=INFO
log4j.logger.org.wso2telco.dep.notifyeventmediator.FaultEventHandler=OFF


# The notify event logger dumps failed events when trying to publish events
# This works only with notifyEvent mediator
log4j.category.NOTIFY_EVENT_LOGGER=INFO, FAILED_NOTIFY_EVENT_APPENDER
log4j.additivity.NOTIFY_EVENT_LOGGER=false

# Appender config to FAILED_NOTIFY_EVENT_APPENDER
log4j.appender.FAILED_NOTIFY_EVENT_APPENDER=org.apache.log4j.DailyRollingFileAppender
log4j.appender.FAILED_NOTIFY_EVENT_APPENDER.File=${carbon.home}/repository/logs/${instance.log}/failed-notify-events${instance.log}.log
log4j.appender.FAILED_NOTIFY_EVENT_APPENDER.Append=true
log4j.appender.FAILED_NOTIFY_EVENT_APPENDER.layout=org.wso2.carbon.utils.logging.TenantAwarePatternLayout
log4j.appender.FAILED_NOTIFY_EVENT_APPENDER.layout.ConversionPattern=[%d] %P%5p {%c} - %x %m %n
log4j.appender.FAILED_NOTIFY_EVENT_APPENDER.layout.TenantPattern=%U%@%D [%T] [%S]
log4j.appender.FAILED_NOTIFY_EVENT_APPENDER.threshold=DEBUG
```

## Syntax

```xml
<publishEvent>
    <eventSink>String</eventSink>
    <streamName>String</streamName>
    <streamVersion>String</streamVersion>
    <enabled>true</enabled>
    <attributes>
        <meta>
            <attribute name="string" type="dataType" default="" (value="literal" | expression="[XPath") />
        </meta>
        <correlation>
             <attribute name="string" type="dataType" default="" (value="literal" | expression="[XPath") />
        </correlation>
        <payload>
             <attribute name="string" type="dataType" default="" (value="literal" | expression="[XPath") />
        </payload>
    <arbitrary>
             <attribute name="string" type="dataType" default="" value="literal" />
        </arbitrary>
    </attributes>
</publishEvent>
```

Parameters that can be configured for the Notify Event mediator are as follows.

| Parameter Name   |  Description  |
| :--------------- | :-----|
| Event Sink       | The name of the event sink to which the events should be published. |
| Stream Name      | The name of the stream to be used for sending data. |
| Stream Version   | The version of the stream to be used for sending data. |
| Enabled          | The state of the mediator. This is an optional parameter and by default value is true |

You can add the following four types of attributes to a Notify Event mediator configuration.

* **Meta Attributes:** The list of attributes which are included in the Meta section of the event.

* **Correlated Attributes:** The list of attributes that are included in the Correlated section of the event.

* **Payload Attributes:** The list of attributes that are included in the Payload section of the event.

* **Arbitrary Attributes:** The list of attributes that are included in the Arbitrary section of the event.

The parameters that are available to configure an individual attribute are as follows.

| Parameter Name   |  Description  |
| :--------------- | :-----|
| Attribute Name   | The name of the attribute. |
| Attribute Value  | This parameter specifies whether the value of the attribute should be a static value or an expression. |
| Value/Expression | The value of the attribute. You can enter a static value, or an expression to evaluate the value depending on the selection made in the **Attribute Value** parameter. |
| Type             | The data type of the attribute. |

## Example
In this configuration, the Notify Event mediator uses four attributes to extract information from the ESB. This information is published in an event sink in the ESB named *sample_event_sink*.

```xml
<publishEvent>
  <eventSink>sample_event_sink</eventSink>
  <streamName>stream_88</streamName>
  <streamVersion>1.1.2</streamVersion>
  <attributes>
     <meta>
        <attribute name="http_method"
                   type="STRING"
                   defaultValue=""
                   expression="get-property('axis2', 'HTTP_METHOD')"/>
        <attribute name="destination"
                   type="STRING"
                   defaultValue=""
                   expression="get-property('To')"/>
     </meta>
     <correlation>
        <attribute name="date"
                   type="STRING"
                   defaultValue=""
                   expression="get-property('SYSTEM_DATE')"/>
     </correlation>
     <payload>
        <attribute xmlns:m0="http://services.samples"
                   name="symbol"
                   type="STRING"
                   defaultValue=""
                   expression="$body/m0:getQuote/m0:request/m0:symbol"/>
     </payload>
  </attributes>
</publishEvent>
```