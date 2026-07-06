package com.example.teamflow.util;

import com.example.teamflow.entity.TaskStatus;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Validates allowed task status transitions and dependency rules.
 */
public final class TaskStatusValidator {

    private static final Map<TaskStatus, Set<TaskStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(TaskStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(TaskStatus.TODO, Set.of(TaskStatus.IN_PROGRESS, TaskStatus.BLOCKED));
        ALLOWED_TRANSITIONS.put(TaskStatus.IN_PROGRESS, Set.of(TaskStatus.IN_REVIEW, TaskStatus.BLOCKED, TaskStatus.TODO));
        ALLOWED_TRANSITIONS.put(TaskStatus.IN_REVIEW, Set.of(TaskStatus.COMPLETED, TaskStatus.IN_PROGRESS));
        ALLOWED_TRANSITIONS.put(TaskStatus.BLOCKED, Set.of(TaskStatus.TODO, TaskStatus.IN_PROGRESS));
        ALLOWED_TRANSITIONS.put(TaskStatus.COMPLETED, Set.of());
    }

    private TaskStatusValidator() {
    }

    public static boolean isValidTransition(TaskStatus current, TaskStatus target) {
        if (current == target) {
            return true;
        }
        Set<TaskStatus> allowed = ALLOWED_TRANSITIONS.get(current);
        return allowed != null && allowed.contains(target);
    }

    public static boolean canStart(TaskStatus dependencyStatus) {
        return dependencyStatus == TaskStatus.COMPLETED;
    }
}
