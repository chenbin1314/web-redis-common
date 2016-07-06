package com.jingoal.web.common;

import java.io.Serializable;

/**
 * Description: TODO
 *
 * @company: 北京今目标信息技术有限公司
 */
public interface Keyable extends Serializable {

  /**
   * Description: 生成缓存Key
   *
   * @return String
   * 
   * @author: chenbin
   * @time: 2016年4月6日 下午7:47:36
   */
  String key();

}
