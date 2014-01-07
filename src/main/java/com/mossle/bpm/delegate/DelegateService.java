
package com.mossle.bpm.delegate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DelegateService {
	private static Logger logger = LoggerFactory.getLogger(DelegateService.class);
    public static final String SQL_GET_DELEGATE_INFO = "select * from bpm_delegate_info"
			+ " where status=1 and assignee=? order by id desc";
    public static final String SQL_SET_DELEGATE_INFO = "insert into bpm_delegate_history"
            + "(assignee,attorney,delegate_time,task_id,status) values(?,?,now(),?,1)";
	private JdbcTemplate jdbcTemplate;

	public DelegateInfo getDelegateInfo(String targetAssignee, String targetProcessDefinitionId) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(SQL_GET_DELEGATE_INFO, targetAssignee);
		for (Map<String, Object> map : list) {
			logger.info("map : {}", map);

			Long id = (Long) map.get("id");
			String assignee = (String) map.get("assignee");
			String attorney = (String) map.get("attorney");
			String processDefinitionId = (String) map
					.get("process_definition_id");
			Date startTime = (Date) map.get("start_time");
			Date endTime = (Date) map.get("end_time");
			Integer status = (Integer) map.get("status");

			if (timeNotBetweenNow(startTime, endTime)) {
				logger.info("timeNotBetweenNow");

				continue;
			}

			if ((processDefinitionId == null)
					|| processDefinitionId.equals(targetProcessDefinitionId)) {
				logger
						.info("delegate to {}",
								attorney);
				//delegateTask.setAssignee(attorney);
				//jdbcTemplate.update(SQL_SET_DELEGATE_INFO, assignee,
				//		attorney, delegateTask.getId());
				DelegateInfo delegateInfo = new DelegateInfo();
				delegateInfo.setId(id);
				delegateInfo.setAssignee(assignee);
				delegateInfo.setAttorney(attorney);
				delegateInfo.setProcessDefinitionId(processDefinitionId);
				delegateInfo.setStartTime(startTime);
				delegateInfo.setEndTime(endTime);
				delegateInfo.setStatus(status);
				return delegateInfo;
			}
		}
		return null;
	}

	public void saveRecord(String assignee, String attorney, String taskId) {
		jdbcTemplate.update(SQL_SET_DELEGATE_INFO, assignee,
				attorney, taskId);
	}

	public void addDelegateInfo(String assignee, String attorney, Date startTime, Date endTime, String processDefinitionId) {
		String sql = "insert into bpm_delegate_info(assignee,attorney,start_time,end_time,process_definition_id,status) values(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, assignee, attorney, startTime, endTime, processDefinitionId, 1);
	}

    private boolean timeNotBetweenNow(Date startTime, Date endTime) {
        if ((startTime == null) && (endTime == null)) {
            return false;
        }

        Date now = new Date();

        return now.before(startTime) || now.after(endTime);
    }

	@Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
