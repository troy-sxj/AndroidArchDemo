## 待完善
 - [ ] 图片加载
 - [ ] 网络请求缓存（无网络是显示缓存页面）

## 项目架构及模块说明

### base 模块
#### 1. libutils 工具模块
> 分为AndroidUtils、JavaUtils

#### 2. libbase 基类模块
> 实现了BaseActivity、BaseFragment、BaseDialogFragment。方便后期基类替换（例如：extends Activity 替换为 extends AppCompatActivity）

**依赖关系：**
    
    api : libutil
    