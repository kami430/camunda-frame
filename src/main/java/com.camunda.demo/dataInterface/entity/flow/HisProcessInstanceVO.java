package com.camunda.demo.dataInterface.entity.flow;

import lombok.Data;
import org.camunda.bpm.engine.history.HistoricProcessInstance;

import java.util.Date;

@Data
public class HisProcessInstanceVO {

    HistoricProcessInstance instance = new HistoricProcessInstance() {
        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getBusinessKey() {
            return null;
        }

        @Override
        public String getProcessDefinitionKey() {
            return null;
        }

        @Override
        public String getProcessDefinitionId() {
            return null;
        }

        @Override
        public String getProcessDefinitionName() {
            return null;
        }

        @Override
        public Integer getProcessDefinitionVersion() {
            return null;
        }

        @Override
        public Date getStartTime() {
            return null;
        }

        @Override
        public Date getEndTime() {
            return null;
        }

        @Override
        public Date getRemovalTime() {
            return null;
        }

        @Override
        public Long getDurationInMillis() {
            return null;
        }

        @Override
        public String getEndActivityId() {
            return null;
        }

        @Override
        public String getStartUserId() {
            return null;
        }

        @Override
        public String getStartActivityId() {
            return null;
        }

        @Override
        public String getDeleteReason() {
            return null;
        }

        @Override
        public String getSuperProcessInstanceId() {
            return null;
        }

        @Override
        public String getRootProcessInstanceId() {
            return null;
        }

        @Override
        public String getSuperCaseInstanceId() {
            return null;
        }

        @Override
        public String getCaseInstanceId() {
            return null;
        }

        @Override
        public String getTenantId() {
            return null;
        }

        @Override
        public String getState() {
            return null;
        }
    };

    public HisProcessInstanceVO(HistoricProcessInstance instance){
        instance.getId();
        instance.getBusinessKey();
        instance.getDeleteReason();
        instance.getRemovalTime();
        instance.getStartTime();
        instance.getEndTime();
        instance.getStartUserId();
        instance.getState();
        instance.getStartActivityId();
    }
}
