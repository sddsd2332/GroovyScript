package com.cleanroommc.groovyscript.core.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class InvokerHelperVisitor extends ClassVisitor implements Opcodes {

    public static final String CLASS_NAME = "org.codehaus.groovy.runtime.InvokerHelper";

    public static final String CREATE_MAP_METHOD = "createMap";

    private static final String LINKED_HASH_MAP_TYPE = "java/util/LinkedHashMap";
    private static final String FAST_UTIL_MAP_TYPE = "it/unimi/dsi/fastutil/objects/Object2ObjectLinkedOpenHashMap";
    private static final String CONSTRUCTOR = "<init>";

    public InvokerHelperVisitor(ClassWriter writer) {
        super(ASM5, writer);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (CREATE_MAP_METHOD.equals(name)) {
            return new CreateMapVisitor(visitor);
        }
        return visitor;
    }

    public static class CreateMapVisitor extends MethodVisitor {

        public CreateMapVisitor(MethodVisitor mv) {
            super(ASM5, mv);
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
            if (opcode == NEW && LINKED_HASH_MAP_TYPE.equals(type)) {
                type = FAST_UTIL_MAP_TYPE;
            }
            super.visitTypeInsn(opcode, type);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (opcode == INVOKESPECIAL && LINKED_HASH_MAP_TYPE.equals(owner) && CONSTRUCTOR.equals(name)) {
                owner = FAST_UTIL_MAP_TYPE;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }
}
