package cn.hyperchain.graphql;

import cn.hyperchain.graphql.vo.UserVO;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2023-02-22 14:41
 **/
public class GraphQLDemo1 {
    public static void main(String[] args) {
    /* schema {
            query:UserQuery
        }
        type UserQuery{
            user:User
        }
        type User{
            id:Long!
            name:String
            age:Integer
        }
        */
        /**
         * type User{#定义对象}
         */
        GraphQLObjectType userObjectType = GraphQLObjectType.newObject()
                .name("User")
                .field(GraphQLFieldDefinition.newFieldDefinition().name("userId").type(Scalars.GraphQLLong))
                .field(GraphQLFieldDefinition.newFieldDefinition().name("userName").type(Scalars.GraphQLString))
                .field(GraphQLFieldDefinition.newFieldDefinition().name("age").type(Scalars.GraphQLInt))
                .build();

        /**
         * user:User #指定对象及查询类型
         */
        GraphQLFieldDefinition userFileldDefinition = GraphQLFieldDefinition.newFieldDefinition()
                .name("getUser")
                .type(userObjectType)
                .argument(GraphQLArgument.newArgument().name("userId").type(Scalars.GraphQLLong).build())
//                .dataFetcher(new StaticDataFetcher( new UserVO(1L,"张双",20)))
                .dataFetcher(dataFetchingEnvironment -> {
                    Long id = dataFetchingEnvironment.getArgument("userId");
                    return new UserVO(id, "张双" + id, 20);//造一个数据？
                })
                .build();

        /**
         * type UserQuery{#定义查询类型}
         */
        GraphQLObjectType userQuery = GraphQLObjectType.newObject()
                .name("UserQuery")
                .field(userFileldDefinition)
                .build();

        /**
         * schema{#定义查询}
         */
        GraphQLSchema graphQLSchema = GraphQLSchema.newSchema().query(userQuery).build();
        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();

        String query = "{getUser(userId:100){userId,userName,age}}";
        ExecutionResult execute = graphQL.execute(query);
        System.out.println(execute.toSpecification());
    }
}