package com.mossle.bpm.cmd;

import java.io.*;

import java.util.*;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.diagram.*;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.cmd.GetBpmnModelCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.*;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;

public class HistoryProcessInstanceDiagramCmd implements Command<InputStream> {
    protected String historyProcessInstanceId;

    public HistoryProcessInstanceDiagramCmd(String historyProcessInstanceId) {
        this.historyProcessInstanceId = historyProcessInstanceId;
    }

    public InputStream execute(CommandContext commandContext) {
        HistoricProcessInstanceEntityManager historicProcessInstanceEntityManager = commandContext
                .getHistoricProcessInstanceEntityManager();
        HistoricProcessInstanceEntity historicProcessInstanceEntity = historicProcessInstanceEntityManager
                .findHistoricProcessInstance(historyProcessInstanceId);

        List<String> activityIds = this
                .getActivityIdsFromHistoricProcessInstanceEntity(
                        historicProcessInstanceEntity, commandContext);

        String processDefinitionId = historicProcessInstanceEntity
                .getProcessDefinitionId();

        ProcessDefinitionEntity processDefinitionEntity = Context
                .getProcessEngineConfiguration().getProcessDefinitionCache()
                .get(processDefinitionId);

        GetBpmnModelCmd getBpmnModelCmd = new GetBpmnModelCmd(
                processDefinitionId);
        BpmnModel bpmnModel = getBpmnModelCmd.execute(commandContext);

        InputStream is = ProcessDiagramGenerator.generateDiagram(
                bpmnModel, "png", activityIds);

        return is;
    }

    protected List<String> getActivityIdsFromHistoricProcessInstanceEntity(
            HistoricProcessInstanceEntity historicProcessInstanceEntity,
            CommandContext commandContext) {
        if (historicProcessInstanceEntity.getEndActivityId() == null) {
            String processInstanceId = historicProcessInstanceEntity.getId();
            ExecutionEntityManager executionEntityManager = commandContext
                    .getExecutionEntityManager();
            ExecutionEntity executionEntity = executionEntityManager
                    .findExecutionById(processInstanceId);

            List<String> activityIds = executionEntity.findActiveActivityIds();

            return activityIds;
        } else {
            return Collections.singletonList(historicProcessInstanceEntity
                    .getEndActivityId());
        }
    }
}
