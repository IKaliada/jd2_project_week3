package com.gmail.rebel249.servicemodule.aspect;

import com.gmail.rebel249.servicemodule.AuditItemService;
import com.gmail.rebel249.servicemodule.model.ItemDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ItemAspect {

    private final AuditItemService auditItemService;
    private static final Logger logger = LogManager.getLogger(ItemAspect.class);

    @Autowired
    public ItemAspect(AuditItemService auditItemService) {
        this.auditItemService = auditItemService;
    }

    @Pointcut("execution(public * com.gmail.rebel249.servicemodule.impl.ItemServiceImpl.update(..))")
    public void insertDate() {
    }

    @AfterReturning(pointcut = "execution(* com.gmail.rebel249.servicemodule.impl.ItemServiceImpl.add(..))",
            returning = "retVal")
    public void logInfoAfterInserting(Object retVal) {
        Long id = ((ItemDTO) retVal).getId();
        String action = "CREATE";
        auditItemService.getAdviseDuringAddItem(action, id);
    }

    @Around("insertDate()")
    public Object aroundInsertDate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnVal = null;
        for (Object argument : joinPoint.getArgs()) {
            logger.info("Item in aspect " + argument);
            if (argument instanceof ItemDTO) {
                ItemDTO itemDTO = (ItemDTO) argument;
                Long id = itemDTO.getId();
                String action = "UPDATE";
                auditItemService.getAdviseDuringUpdateItem(action, id);
            }
            try {
                returnVal = joinPoint.proceed();
            } catch (Throwable e) {
                logger.debug("Unable to use JointPoint :(");
            }
        }
        return returnVal;
    }
}
