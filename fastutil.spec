%define name		fastUtil
%define version		2.51
%define release		1jpp
%define javadir		%{_datadir}/java
%define javadocdir	%{_datadir}/javadoc
%define section		free

Name:           %{name}
Version:        %{version}
Release:        %{release}
Summary:        fastUtil: Fast & compact type-specific utility classes
License:        LGPL
Source0:	http://vigna.dsi.unimi.it/fastUtil/%{name}-%{version}.tar.gz
URL:            http://vigna.dsi.unimi.it/%{name}/
Group:          Development/Libraries/Java
Vendor:         JPackage Project
Distribution:   JPackage
BuildRequires:  ant, make, gcc
BuildArch:      noarch
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-buildroot
Provides:	%{name}

%description 
fastUtil provides type-specific maps and sets with a small memory footprint and
much (up to 10 times) faster access and insertion.  The classes implement their
standard counterpart interface (e.g., Map for maps) and can be plugged into
existing code. Moreover, they provide additional features (such as
bidirectional iterators) that are not available in the standard classes.

%package manual
Summary:        Manual for %{name}
Group:          Development/Java

%description manual
Documentation for %{name}.

%package javadoc
Summary:        Javadoc for %{name}
Group:          Development/Java

%description javadoc
Javadoc for %{name}.

%prep
rm -rf $RPM_BUILD_ROOT
%setup -n %{name}-%{version}

%build
./gencsources.sh
make jar

%install
#jars
install -d -m 755 $RPM_BUILD_ROOT%{javadir}
install -m 644 %{name}-%{version}.jar $RPM_BUILD_ROOT%{javadir}/
(cd $RPM_BUILD_ROOT%{javadir} && for jar in *-%{version}*; do ln -sf ${jar} `echo $jar| sed  "s|-%{version}||g"`; done)
#javadoc
install -d -m 755 $RPM_BUILD_ROOT%{javadocdir}/%{name}-%{version}
cp -pr docs/* $RPM_BUILD_ROOT%{javadocdir}/%{name}-%{version}
(cd $RPM_BUILD_ROOT%{javadocdir} && for docdir in *-%{version}*; do ln -sf ${docdir} `echo $docdir| sed  "s|-%{version}||g"`; done)

%clean
rm -rf $RPM_BUILD_ROOT

%files
%defattr(0644,root,root,0755)
%{javadir}/*

%files manual
%defattr(0644,root,root,0755)
%doc README CHANGES COPYING.LIB

%files javadoc
%defattr(0644,root,root,0755)
%{javadocdir}/%{name}-%{version}
%{javadocdir}/%{name}

%changelog
* Wed Feb 26 2003 Sebastiano Vigna <vigna at acm.org> 2.51
- New flexible trim(int) method..
* Tue Feb 18 2003 Sebastiano Vigna <vigna at acm.org> 2.50
- New linked hash tables and reference-based containers.
* Mon Dec 9 2002 Sebastiano Vigna <vigna at acm.org> 2.11
- JPackage compatible version
