package top.miqiu;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class ResourceLoader {
    
        public static Map<String, BeanDefinition> getResource() throws IOException {
            Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>(16);
            Properties properties = new Properties();

            try {
                InputStream inputStream = ResourceLoader.class.getResourceAsStream("/beans.properties");
                properties.load(inputStream);
                Iterator<String> it = properties.stringPropertyNames().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    String className = properties.getProperty(key);
                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setBeanName(key);
                    Class clazz = Class.forName(className);
                    beanDefinition.setBeanClass(clazz);
                    beanDefinitionMap.put(key, beanDefinition);
                }
                inputStream.close();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return beanDefinitionMap;
        }
    /**
     * # 代码解释
     * 这段代码的功能是从`beans.properties`文件中加载配置，将每个键值对映射为一个`BeanDefinition`对象，并存储到`Map<String, BeanDefinition>`中。具体逻辑如下：
     * 1. 初始化一个`HashMap`用于存储`BeanDefinition`对象。
     * 2. 使用`Properties`类加载`beans.properties`文件的内容。
     * 3. 遍历`properties`中的所有键值对，对于每个键值对：
     *    - 获取键作为`beanName`，值作为`className`。
     *    - 创建一个`BeanDefinition`对象，设置其`beanName`和`beanClass`。
     *    - 将该`BeanDefinition`对象存入`beanDefinitionMap`。
     * 4. 关闭输入流。
     * 5. 如果出现`ClassNotFoundException`，抛出运行时异常。
     * 6. 返回构造好的`beanDefinitionMap`。
     *
     * # 控制流图
     * ```mermaid
     * flowchart TD
     *     A[开始] --> B[初始化beanDefinitionMap和properties]
     *     B --> C[读取beans.properties文件]
     *     C --> D{是否读取成功？}
     *     D -->|No| F[抛出IOException]
     *     D -->|Yes| G[遍历properties的键值对]
     *     G --> H{是否有下一个键？}
     *     H -->|No| K[关闭输入流]
     *     H -->|Yes| I[获取键和对应的类名]
     *     I --> J[创建BeanDefinition并设置属性]
     *     J --> G
     *     K --> L[返回beanDefinitionMap]
     * ```
     */

}
