
  Pod::Spec.new do |s|
    s.name = 'JuganuAccesspoint'
    s.version = '0.0.1'
    s.summary = 'Juganu widget to manage connections to Access Points'
    s.license = 'MIT'
    s.homepage = 'https://github.com/juganu-brighter/plugin-accesspoint-connection.git'
    s.author = 'Juganu'
    s.source = { :git => 'https://github.com/juganu-brighter/plugin-accesspoint-connection.git', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end