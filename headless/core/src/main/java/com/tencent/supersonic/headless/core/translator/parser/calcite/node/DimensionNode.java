package com.tencent.supersonic.headless.core.translator.parser.calcite.node;

import com.tencent.supersonic.common.pojo.enums.DataTypeEnums;
import com.tencent.supersonic.common.pojo.enums.EngineType;
import com.tencent.supersonic.headless.api.pojo.response.DimSchemaResp;
import com.tencent.supersonic.headless.core.translator.parser.s2sql.Constants;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.validate.SqlValidatorScope;

import java.util.List;
import java.util.Objects;

public class DimensionNode extends SemanticNode {

    public static SqlNode build(DimSchemaResp dimension, SqlValidatorScope scope,
            EngineType engineType) throws Exception {
        return parse(dimension.getExpr(), scope, engineType);
    }

    public static List<SqlNode> expand(DimSchemaResp dimension, SqlValidatorScope scope,
            EngineType engineType) throws Exception {
        SqlNode sqlNode = parse(dimension.getExpr(), scope, engineType);
        return expand(sqlNode, scope);
    }

    public static SqlNode buildName(DimSchemaResp dimension, SqlValidatorScope scope,
            EngineType engineType) throws Exception {
        return parse(dimension.getExpr(), scope, engineType);
    }

    public static SqlNode buildNameAs(String alias, DimSchemaResp dimension,
            SqlValidatorScope scope, EngineType engineType) throws Exception {
        if ("".equals(alias)) {
            return buildName(dimension, scope, engineType);
        }
        SqlNode sqlNode = parse(dimension.getExpr(), scope, engineType);
        return buildAs(alias, sqlNode);
    }

    public static SqlNode buildArray(DimSchemaResp dimension, SqlValidatorScope scope,
            EngineType engineType) throws Exception {
        if (Objects.nonNull(dimension.getDataType())
                && dimension.getDataType().equals(DataTypeEnums.ARRAY)) {
            SqlNode sqlNode = parse(dimension.getExpr(), scope, engineType);
            if (isIdentifier(sqlNode)) {
                return buildAs(dimension.getName(),
                        parse(dimension.getExpr() + Constants.DIMENSION_ARRAY_SINGLE_SUFFIX, scope,
                                engineType));
            }
            throw new Exception("array dimension expr should only identify");
        }
        return build(dimension, scope, engineType);
    }
}
