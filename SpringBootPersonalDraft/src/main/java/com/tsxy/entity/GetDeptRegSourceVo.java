package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Liu_df
 * @Date 2022/10/11 21:55
 */

public class GetDeptRegSourceVo implements Serializable {

    /**
     * 号源详细信息
     */
    private List<entity.DeptRegSourceVo> regSources = new ArrayList<>();

    /** 拓展字段 */
    private Map<String, String> extPropes;

    public Map<String, String> getExtPropes() {
        return extPropes;
    }

    public void setExtPropes(Map<String, String> extPropes) {
        this.extPropes = extPropes;
    }

    public List<entity.DeptRegSourceVo> getRegSources() {
        return regSources;
    }

    public void setRegSources(List<entity.DeptRegSourceVo> regSources) {
        this.regSources = regSources;
    }

}
