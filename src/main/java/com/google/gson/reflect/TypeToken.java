package com.google.gson.reflect;

import com.google.gson.internal.C$Gson$Preconditions;
import com.google.gson.internal.C$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class TypeToken<T> {
    final int hashCode = this.type.hashCode();
    final Class<? super T> rawType;
    final Type type;

    protected TypeToken() {
        Type superclassTypeParameter = getSuperclassTypeParameter(getClass());
        this.type = superclassTypeParameter;
        this.rawType = C$Gson$Types.getRawType(superclassTypeParameter);
    }

    TypeToken(Type type2) {
        Type canonicalize = C$Gson$Types.canonicalize((Type) C$Gson$Preconditions.checkNotNull(type2));
        this.type = canonicalize;
        this.rawType = C$Gson$Types.getRawType(canonicalize);
    }

    static Type getSuperclassTypeParameter(Class<?> cls) {
        Type genericSuperclass = cls.getGenericSuperclass();
        if (!(genericSuperclass instanceof Class)) {
            return C$Gson$Types.canonicalize(((ParameterizedType) genericSuperclass).getActualTypeArguments()[0]);
        }
        throw new RuntimeException("Missing type parameter.");
    }

    public final Class<? super T> getRawType() {
        return this.rawType;
    }

    public final Type getType() {
        return this.type;
    }

    @Deprecated
    public boolean isAssignableFrom(Class<?> cls) {
        return isAssignableFrom((Type) cls);
    }

    @Deprecated
    public boolean isAssignableFrom(Type type2) {
        if (type2 == null) {
            return false;
        }
        if (this.type.equals(type2)) {
            return true;
        }
        Type type3 = this.type;
        if (type3 instanceof Class) {
            return this.rawType.isAssignableFrom(C$Gson$Types.getRawType(type2));
        }
        if (type3 instanceof ParameterizedType) {
            return isAssignableFrom(type2, (ParameterizedType) type3, new HashMap());
        }
        if (!(type3 instanceof GenericArrayType)) {
            throw buildUnexpectedTypeError(type3, Class.class, ParameterizedType.class, GenericArrayType.class);
        } else if (!this.rawType.isAssignableFrom(C$Gson$Types.getRawType(type2)) || !isAssignableFrom(type2, (GenericArrayType) this.type)) {
            return false;
        } else {
            return true;
        }
    }

    @Deprecated
    public boolean isAssignableFrom(TypeToken<?> typeToken) {
        return isAssignableFrom(typeToken.getType());
    }

    /* JADX INFO: additional move instructions added (1) to help type inference */
    /* JADX WARN: Failed to insert an additional move for type inference into block B:14:0x0024 */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.lang.reflect.Type] */
    /* JADX WARN: Type inference failed for: r1v9 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isAssignableFrom(java.lang.reflect.Type r1, java.lang.reflect.GenericArrayType r2) {
        /*
            java.lang.reflect.Type r2 = r2.getGenericComponentType()
            boolean r0 = r2 instanceof java.lang.reflect.ParameterizedType
            if (r0 == 0) goto L_0x0030
            boolean r0 = r1 instanceof java.lang.reflect.GenericArrayType
            if (r0 == 0) goto L_0x0013
            java.lang.reflect.GenericArrayType r1 = (java.lang.reflect.GenericArrayType) r1
            java.lang.reflect.Type r1 = r1.getGenericComponentType()
            goto L_0x0024
        L_0x0013:
            boolean r0 = r1 instanceof java.lang.Class
            if (r0 == 0) goto L_0x0024
            java.lang.Class r1 = (java.lang.Class) r1
        L_0x0019:
            boolean r0 = r1.isArray()
            if (r0 == 0) goto L_0x0024
            java.lang.Class r1 = r1.getComponentType()
            goto L_0x0019
        L_0x0024:
            java.lang.reflect.ParameterizedType r2 = (java.lang.reflect.ParameterizedType) r2
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            boolean r1 = isAssignableFrom(r1, r2, r0)
            return r1
        L_0x0030:
            r1 = 1
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.reflect.TypeToken.isAssignableFrom(java.lang.reflect.Type, java.lang.reflect.GenericArrayType):boolean");
    }

    private static boolean isAssignableFrom(Type type2, ParameterizedType parameterizedType, Map<String, Type> map) {
        if (type2 == null) {
            return false;
        }
        if (parameterizedType.equals(type2)) {
            return true;
        }
        Class<?> rawType2 = C$Gson$Types.getRawType(type2);
        ParameterizedType parameterizedType2 = null;
        if (type2 instanceof ParameterizedType) {
            parameterizedType2 = (ParameterizedType) type2;
        }
        if (parameterizedType2 != null) {
            Type[] actualTypeArguments = parameterizedType2.getActualTypeArguments();
            TypeVariable[] typeParameters = rawType2.getTypeParameters();
            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type type3 = actualTypeArguments[i];
                TypeVariable typeVariable = typeParameters[i];
                while (type3 instanceof TypeVariable) {
                    type3 = map.get(((TypeVariable) type3).getName());
                }
                map.put(typeVariable.getName(), type3);
            }
            if (typeEquals(parameterizedType2, parameterizedType, map)) {
                return true;
            }
        }
        for (Type isAssignableFrom : rawType2.getGenericInterfaces()) {
            if (isAssignableFrom(isAssignableFrom, parameterizedType, new HashMap(map))) {
                return true;
            }
        }
        return isAssignableFrom(rawType2.getGenericSuperclass(), parameterizedType, new HashMap(map));
    }

    private static boolean typeEquals(ParameterizedType parameterizedType, ParameterizedType parameterizedType2, Map<String, Type> map) {
        if (!parameterizedType.getRawType().equals(parameterizedType2.getRawType())) {
            return false;
        }
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();
        for (int i = 0; i < actualTypeArguments.length; i++) {
            if (!matches(actualTypeArguments[i], actualTypeArguments2[i], map)) {
                return false;
            }
        }
        return true;
    }

    private static AssertionError buildUnexpectedTypeError(Type type2, Class<?>... clsArr) {
        StringBuilder sb = new StringBuilder("Unexpected type. Expected one of: ");
        for (Class<?> name : clsArr) {
            sb.append(name.getName());
            sb.append(", ");
        }
        sb.append("but got: ");
        sb.append(type2.getClass().getName());
        sb.append(", for type token: ");
        sb.append(type2.toString());
        sb.append('.');
        return new AssertionError(sb.toString());
    }

    private static boolean matches(Type type2, Type type3, Map<String, Type> map) {
        return type3.equals(type2) || ((type2 instanceof TypeVariable) && type3.equals(map.get(((TypeVariable) type2).getName())));
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public final boolean equals(Object obj) {
        return (obj instanceof TypeToken) && C$Gson$Types.equals(this.type, ((TypeToken) obj).type);
    }

    public final String toString() {
        return C$Gson$Types.typeToString(this.type);
    }

    public static TypeToken<?> get(Type type2) {
        return new TypeToken<>(type2);
    }

    public static <T> TypeToken<T> get(Class cls) {
        return new TypeToken<>(cls);
    }

    public static TypeToken<?> getParameterized(Type type2, Type... typeArr) {
        return new TypeToken<>(C$Gson$Types.newParameterizedTypeWithOwner(null, type2, typeArr));
    }

    public static TypeToken<?> getArray(Type type2) {
        return new TypeToken<>(C$Gson$Types.arrayOf(type2));
    }
}