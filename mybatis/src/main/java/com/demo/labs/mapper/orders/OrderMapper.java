package com.demo.labs.mapper.orders;

import com.demo.labs.dataobject.OrderDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper {
    OrderDO selectById(@Param("id") Integer id);
}
