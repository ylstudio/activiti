import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.task.DelegationState;

/**
 * @author yangwm
 * @since 2013-10-14
 */
public class Test {

	public static void main(String[] args) {
		System.out.println(DelegationState.PENDING.name());
		System.out.println(DelegationState.RESOLVED.name());
	}

}
