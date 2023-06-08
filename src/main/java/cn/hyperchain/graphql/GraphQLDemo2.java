package cn.hyperchain.graphql;

import cn.hutool.core.io.IoUtil;
import cn.hyperchain.graphql.vo.ResultModel;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2023-02-22 14:52
 **/
public class GraphQLDemo2 {
    public static void main(String[] args) throws  IOException {
        String fileName = "graphqls/user.graphql";
        String fileContent = IoUtil.read(GraphQLDemo2.class.getClassLoader().getResource(fileName).openStream(),"utf-8");
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(fileContent);
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring().type("userMutations", builder ->
                builder.dataFetcher("addUser", dataFetchingEnvironment -> {
                    Map<String, Object> arguments = dataFetchingEnvironment.getArguments();
                    System.out.println("新增参数：" + arguments);
                    return new ResultModel(200, "新增成功", "");
                })
        ).build();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        //定义新增参数
        Map<String, Object> param = new HashMap<>();
        Map userInfo = new HashMap();
        userInfo.put("userName", "王小二");
        userInfo.put("age", 3);
        param.put("saveParam", userInfo);
        //定义graphql输入参数
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .variables(param)
                .query("mutation addUser($saveParam:UserSaveDTO){addUser(addUser:$saveParam){code,msg}}")
                .build();
        ExecutionResult execute = graphQL.execute(executionInput);
        System.out.println(execute.toSpecification());
    }
}