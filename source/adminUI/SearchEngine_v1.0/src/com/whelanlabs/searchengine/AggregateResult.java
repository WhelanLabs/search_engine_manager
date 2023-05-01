/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.whelanlabs.searchengine;

import java.util.HashMap;

/**
 *
 * @author john
 */
class AggregateResult {

   public String _key;
   public Integer _size;
   public HashMap<Integer, Integer> _flow;
   
           
   AggregateResult(String key, Integer size, HashMap<Integer, Integer> flow) {
      _key = key;
      _size = size;
      _flow = flow;
   }

}
