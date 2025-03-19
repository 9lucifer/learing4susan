### **🚀 手写 IOC 容器的实现流程**
**本方案模仿 Spring IOC 实现了一个简单的依赖注入容器，核心流程如下：**

---

## **📌 1️⃣ 核心组件**
| **组件** | **作用** |
|----------|----------|
| **BeanDefinition** | Bean 的元数据定义，包括 `beanName` 和 `Class` |
| **ResourceLoader** | 资源加载器，解析 `beans.properties` 配置文件 |
| **BeanRegister** | 单例 Bean 的缓存，管理 Bean 的实例 |
| **BeanFactory** | 核心 Bean 工厂，负责创建和获取 Bean |
| **UserDao** | 示例业务 Bean |

---

## **📌 2️⃣ 实现流程**
### **🔹 第一步：Bean 定义**
- `BeanDefinition.java` **用于存储 Bean 的基本信息（名称、类名）**
```java
public class BeanDefinition {
    private String beanName;
    private Class<?> beanClass;

    public String getBeanName() { return beanName; }
    public void setBeanName(String beanName) { this.beanName = beanName; }

    public Class<?> getBeanClass() { return beanClass; }
    public void setBeanClass(Class<?> beanClass) { this.beanClass = beanClass; }
}
```

---

### **🔹 第二步：资源加载**
- `ResourceLoader.java` **解析 `beans.properties`，读取 Bean 定义**
```java
public class ResourceLoader {
    public static Map<String, BeanDefinition> getResource() {
        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
        Properties properties = new Properties();
        try {
            InputStream inputStream = ResourceLoader.class.getResourceAsStream("/beans.properties");
            properties.load(inputStream);

            for (String key : properties.stringPropertyNames()) {
                String className = properties.getProperty(key);
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setBeanName(key);
                beanDefinition.setBeanClass(Class.forName(className));
                beanDefinitionMap.put(key, beanDefinition);
            }
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return beanDefinitionMap;
    }
}
```

---

### **🔹 第三步：Bean 单例缓存**
- `BeanRegister.java` **管理单例 Bean**
```java
public class BeanRegister {
    private Map<String, Object> singletonMap = new HashMap<>();

    public Object getSingletonBean(String beanName) {
        return singletonMap.get(beanName);
    }

    public void registerSingletonBean(String beanName, Object bean) {
        if (!singletonMap.containsKey(beanName)) {
            singletonMap.put(beanName, bean);
        }
    }
}
```

---

### **🔹 第四步：IOC 容器**
- `BeanFactory.java` **核心 Bean 管理工厂**
```java
public class BeanFactory {
    private Map<String, BeanDefinition> beanDefinitionMap;
    private BeanRegister beanRegister;

    public BeanFactory() {
        this.beanRegister = new BeanRegister();
        this.beanDefinitionMap = ResourceLoader.getResource();
    }

    public Object getBean(String beanName) {
        Object bean = beanRegister.getSingletonBean(beanName);
        if (bean != null) {
            return bean;
        }
        return createBean(beanDefinitionMap.get(beanName));
    }

    private Object createBean(BeanDefinition beanDefinition) {
        try {
            Object bean = beanDefinition.getBeanClass().newInstance();
            beanRegister.registerSingletonBean(beanDefinition.getBeanName(), bean);
            return bean;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```

---

### **🔹 第五步：业务 Bean**
- `UserDao.java` **示例 Bean**
```java
public class UserDao {
    public void queryUserInfo() {
        System.out.println("A good man.");
    }
}
```

---

### **🔹 第六步：测试**
```java
public class TestIOC {
    public static void main(String[] args) {
        BeanFactory beanFactory = new BeanFactory();
        UserDao userDao = (UserDao) beanFactory.getBean("userDao");
        userDao.queryUserInfo();
    }
}
```

**📌 运行结果**
```plaintext
A good man.
```

---

## **📌 3️⃣ 总结**
### **🌟 核心流程**
1. **定义 Bean（BeanDefinition）**  
2. **加载配置（ResourceLoader 解析 `beans.properties`）**  
3. **Bean 缓存（BeanRegister 维护单例 Bean）**  
4. **Bean 工厂（BeanFactory 负责创建和管理 Bean）**  
5. **获取 Bean（通过 `getBean` 获取或创建实例）**  

### **🌟 关键点**
✅ **单例管理**（`BeanRegister` 缓存实例）  
✅ **动态加载 Bean**（`ResourceLoader` 解析 `properties`）  
✅ **反射创建对象**（`Class.forName()` + `newInstance()`）  
✅ **保证 Bean 只创建一次**（先查缓存，未找到才创建）  

🚀 **这是一个极简版的 Spring IOC 容器，核心思路和 Spring 非常相似！🔥**
