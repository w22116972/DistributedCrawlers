// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: application.proto

package serialization.protobuf;

/**
 * Protobuf type {@code Websites}
 */
public  final class Websites extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:Websites)
    WebsitesOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Websites.newBuilder() to construct.
  private Websites(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Websites() {
    website_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new Websites();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Websites(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              website_ = new java.util.ArrayList<serialization.protobuf.Website>();
              mutable_bitField0_ |= 0x00000001;
            }
            website_.add(
                input.readMessage(serialization.protobuf.Website.parser(), extensionRegistry));
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        website_ = java.util.Collections.unmodifiableList(website_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return serialization.protobuf.CrawlerProto.internal_static_Websites_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return serialization.protobuf.CrawlerProto.internal_static_Websites_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            serialization.protobuf.Websites.class, serialization.protobuf.Websites.Builder.class);
  }

  public static final int WEBSITE_FIELD_NUMBER = 1;
  private java.util.List<serialization.protobuf.Website> website_;
  /**
   * <code>repeated .Website website = 1;</code>
   */
  public java.util.List<serialization.protobuf.Website> getWebsiteList() {
    return website_;
  }
  /**
   * <code>repeated .Website website = 1;</code>
   */
  public java.util.List<? extends serialization.protobuf.WebsiteOrBuilder> 
      getWebsiteOrBuilderList() {
    return website_;
  }
  /**
   * <code>repeated .Website website = 1;</code>
   */
  public int getWebsiteCount() {
    return website_.size();
  }
  /**
   * <code>repeated .Website website = 1;</code>
   */
  public serialization.protobuf.Website getWebsite(int index) {
    return website_.get(index);
  }
  /**
   * <code>repeated .Website website = 1;</code>
   */
  public serialization.protobuf.WebsiteOrBuilder getWebsiteOrBuilder(
      int index) {
    return website_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < website_.size(); i++) {
      output.writeMessage(1, website_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < website_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, website_.get(i));
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof serialization.protobuf.Websites)) {
      return super.equals(obj);
    }
    serialization.protobuf.Websites other = (serialization.protobuf.Websites) obj;

    if (!getWebsiteList()
        .equals(other.getWebsiteList())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getWebsiteCount() > 0) {
      hash = (37 * hash) + WEBSITE_FIELD_NUMBER;
      hash = (53 * hash) + getWebsiteList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static serialization.protobuf.Websites parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static serialization.protobuf.Websites parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static serialization.protobuf.Websites parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static serialization.protobuf.Websites parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static serialization.protobuf.Websites parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static serialization.protobuf.Websites parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static serialization.protobuf.Websites parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static serialization.protobuf.Websites parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static serialization.protobuf.Websites parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static serialization.protobuf.Websites parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static serialization.protobuf.Websites parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static serialization.protobuf.Websites parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(serialization.protobuf.Websites prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code Websites}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:Websites)
      serialization.protobuf.WebsitesOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return serialization.protobuf.CrawlerProto.internal_static_Websites_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return serialization.protobuf.CrawlerProto.internal_static_Websites_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              serialization.protobuf.Websites.class, serialization.protobuf.Websites.Builder.class);
    }

    // Construct using serialization.protobuf.Websites.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getWebsiteFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (websiteBuilder_ == null) {
        website_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        websiteBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return serialization.protobuf.CrawlerProto.internal_static_Websites_descriptor;
    }

    @java.lang.Override
    public serialization.protobuf.Websites getDefaultInstanceForType() {
      return serialization.protobuf.Websites.getDefaultInstance();
    }

    @java.lang.Override
    public serialization.protobuf.Websites build() {
      serialization.protobuf.Websites result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public serialization.protobuf.Websites buildPartial() {
      serialization.protobuf.Websites result = new serialization.protobuf.Websites(this);
      int from_bitField0_ = bitField0_;
      if (websiteBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          website_ = java.util.Collections.unmodifiableList(website_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.website_ = website_;
      } else {
        result.website_ = websiteBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof serialization.protobuf.Websites) {
        return mergeFrom((serialization.protobuf.Websites)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(serialization.protobuf.Websites other) {
      if (other == serialization.protobuf.Websites.getDefaultInstance()) return this;
      if (websiteBuilder_ == null) {
        if (!other.website_.isEmpty()) {
          if (website_.isEmpty()) {
            website_ = other.website_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureWebsiteIsMutable();
            website_.addAll(other.website_);
          }
          onChanged();
        }
      } else {
        if (!other.website_.isEmpty()) {
          if (websiteBuilder_.isEmpty()) {
            websiteBuilder_.dispose();
            websiteBuilder_ = null;
            website_ = other.website_;
            bitField0_ = (bitField0_ & ~0x00000001);
            websiteBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getWebsiteFieldBuilder() : null;
          } else {
            websiteBuilder_.addAllMessages(other.website_);
          }
        }
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      serialization.protobuf.Websites parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (serialization.protobuf.Websites) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<serialization.protobuf.Website> website_ =
      java.util.Collections.emptyList();
    private void ensureWebsiteIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        website_ = new java.util.ArrayList<serialization.protobuf.Website>(website_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        serialization.protobuf.Website, serialization.protobuf.Website.Builder, serialization.protobuf.WebsiteOrBuilder> websiteBuilder_;

    /**
     * <code>repeated .Website website = 1;</code>
     */
    public java.util.List<serialization.protobuf.Website> getWebsiteList() {
      if (websiteBuilder_ == null) {
        return java.util.Collections.unmodifiableList(website_);
      } else {
        return websiteBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public int getWebsiteCount() {
      if (websiteBuilder_ == null) {
        return website_.size();
      } else {
        return websiteBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public serialization.protobuf.Website getWebsite(int index) {
      if (websiteBuilder_ == null) {
        return website_.get(index);
      } else {
        return websiteBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public Builder setWebsite(
        int index, serialization.protobuf.Website value) {
      if (websiteBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureWebsiteIsMutable();
        website_.set(index, value);
        onChanged();
      } else {
        websiteBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public Builder setWebsite(
        int index, serialization.protobuf.Website.Builder builderForValue) {
      if (websiteBuilder_ == null) {
        ensureWebsiteIsMutable();
        website_.set(index, builderForValue.build());
        onChanged();
      } else {
        websiteBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public Builder addWebsite(serialization.protobuf.Website value) {
      if (websiteBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureWebsiteIsMutable();
        website_.add(value);
        onChanged();
      } else {
        websiteBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public Builder addWebsite(
        int index, serialization.protobuf.Website value) {
      if (websiteBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureWebsiteIsMutable();
        website_.add(index, value);
        onChanged();
      } else {
        websiteBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public Builder addWebsite(
        serialization.protobuf.Website.Builder builderForValue) {
      if (websiteBuilder_ == null) {
        ensureWebsiteIsMutable();
        website_.add(builderForValue.build());
        onChanged();
      } else {
        websiteBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public Builder addWebsite(
        int index, serialization.protobuf.Website.Builder builderForValue) {
      if (websiteBuilder_ == null) {
        ensureWebsiteIsMutable();
        website_.add(index, builderForValue.build());
        onChanged();
      } else {
        websiteBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public Builder addAllWebsite(
        java.lang.Iterable<? extends serialization.protobuf.Website> values) {
      if (websiteBuilder_ == null) {
        ensureWebsiteIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, website_);
        onChanged();
      } else {
        websiteBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public Builder clearWebsite() {
      if (websiteBuilder_ == null) {
        website_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        websiteBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public Builder removeWebsite(int index) {
      if (websiteBuilder_ == null) {
        ensureWebsiteIsMutable();
        website_.remove(index);
        onChanged();
      } else {
        websiteBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public serialization.protobuf.Website.Builder getWebsiteBuilder(
        int index) {
      return getWebsiteFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public serialization.protobuf.WebsiteOrBuilder getWebsiteOrBuilder(
        int index) {
      if (websiteBuilder_ == null) {
        return website_.get(index);  } else {
        return websiteBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public java.util.List<? extends serialization.protobuf.WebsiteOrBuilder> 
         getWebsiteOrBuilderList() {
      if (websiteBuilder_ != null) {
        return websiteBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(website_);
      }
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public serialization.protobuf.Website.Builder addWebsiteBuilder() {
      return getWebsiteFieldBuilder().addBuilder(
          serialization.protobuf.Website.getDefaultInstance());
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public serialization.protobuf.Website.Builder addWebsiteBuilder(
        int index) {
      return getWebsiteFieldBuilder().addBuilder(
          index, serialization.protobuf.Website.getDefaultInstance());
    }
    /**
     * <code>repeated .Website website = 1;</code>
     */
    public java.util.List<serialization.protobuf.Website.Builder> 
         getWebsiteBuilderList() {
      return getWebsiteFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        serialization.protobuf.Website, serialization.protobuf.Website.Builder, serialization.protobuf.WebsiteOrBuilder> 
        getWebsiteFieldBuilder() {
      if (websiteBuilder_ == null) {
        websiteBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            serialization.protobuf.Website, serialization.protobuf.Website.Builder, serialization.protobuf.WebsiteOrBuilder>(
                website_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        website_ = null;
      }
      return websiteBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:Websites)
  }

  // @@protoc_insertion_point(class_scope:Websites)
  private static final serialization.protobuf.Websites DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new serialization.protobuf.Websites();
  }

  public static serialization.protobuf.Websites getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Websites>
      PARSER = new com.google.protobuf.AbstractParser<Websites>() {
    @java.lang.Override
    public Websites parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Websites(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Websites> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Websites> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public serialization.protobuf.Websites getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

