package com.sunnywr.service;

import com.sunnywr.dto.Exposer;
import com.sunnywr.dto.SeckillExecution;
import com.sunnywr.entity.Seckill;
import com.sunnywr.exception.RepeatKillException;
import com.sunnywr.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:spring/spring-dao.xml",
    "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list = {}", list);
    }

    @Test
    public void getById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill = {}", seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1000L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer = {}" ,exposer);
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1000L;
        long phone = 12345123456L;
        String md5 = "b8dd3449dab22538bc7c54b0934eb80d";
        try {
            SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
            logger.info("result = {}", execution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 完整业务逻辑测试
     * @throws Exception
     */
    @Test
    public void testSeckillLogic() throws Exception {
        long id = 1001L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()) {
            logger.info("exposer = {}" ,exposer);
            long phone = 12345123456L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
                logger.info("result = {}", execution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            // 秒杀未开始
            logger.warn("exposer = {}" ,exposer);
        }
    }

    @Test
    public void testExcuteSeckillProcedure() {
        long seckillId = 1003;
        long phone = 12345612345L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }
    }
}