%define section free

Name:           fastutil
Version:        3.0.1
Release:        1jpp
Epoch:          0
Summary:        Fast & compact type-specific Java utility classes
License:        LGPL
Source0:        http://fastutil.dsi.unimi.it/fastutil-3.0.1-src.tar.gz
URL:            http://fastutil.dsi.unimi.it/
Group:          Development/Libraries/Java
Vendor:         JPackage Project
Distribution:   JPackage
BuildRequires:  ant, make, gcc, jpackage-utils >= 0:1.5, /bin/bash
BuildArch:      noarch
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-buildroot

%description

fastutil provides type-specific maps, sets and lists with a small memory
footprint and much faster access and insertion. The classes implement their
standard counterpart interface (e.g., Map for maps) and can be plugged into
existing code. Moreover, they provide additional features (such as
bidirectional iterators) that are not available in the standard classes.

%package javadoc
Summary:        Javadoc for %{name}
Group:          Development/Documentation

%description javadoc
Javadoc for %{name}.

# -----------------------------------------------------------------------------

%prep
%setup -q

# -----------------------------------------------------------------------------

%build

. %{_datadir}/java-utils/java-functions
set_jvm

export PATH=$JAVA_HOME/bin:$PATH
export APIURL=http://java.sun.com/j2se/1.4.1/docs/api/
#export ANT_ARGS="$ANT_ARGS -Dbuild.compiler=jikes"

./gencsources.sh
make jar docs

# -----------------------------------------------------------------------------

%install
rm -rf $RPM_BUILD_ROOT

# jars
mkdir -p $RPM_BUILD_ROOT%{_javadir}
cp -p %{name}-%{version}.jar $RPM_BUILD_ROOT%{_javadir}/%{name}-%{version}.jar
(cd $RPM_BUILD_ROOT%{_javadir} && for jar in *-%{version}*; do ln -sf ${jar} `echo $jar| sed "s|-%{version}||g"`; done)

# javadoc
mkdir -p $RPM_BUILD_ROOT%{_javadocdir}/%{name}-%{version}
cp -pr docs/* $RPM_BUILD_ROOT%{_javadocdir}/%{name}-%{version}
rm -fr docs

# -----------------------------------------------------------------------------

%clean
rm -rf $RPM_BUILD_ROOT

# -----------------------------------------------------------------------------

%post javadoc
rm -f %{_javadocdir}/%{name}
ln -s %{name}-%{version} %{_javadocdir}/%{name}
 
%postun javadoc
if [ "$1" = "0" ]; then
    rm -f %{_javadocdir}/%{name}
fi
 
# -----------------------------------------------------------------------------

%files
%defattr(0644,root,root,0755)
%doc README CHANGES COPYING.LIB
%{_javadir}/*

%files javadoc
%defattr(0644,root,root,0755)
%{_javadocdir}/%{name}-%{version}

# -----------------------------------------------------------------------------

%changelog
* Thu Jun 26 2003 Sebastiano Vigna <vigna at acm.org> - 3.0.1-1jpp
- A small but important serialisation bug fix.

* Thu Jun 26 2003 Sebastiano Vigna <vigna at acm.org> - 3.0-1jpp
- First release of version 3.0.

* Sun Apr 06 2003 Sebastiano Vigna <vigna at acm.org> - 2.60-1jpp
- Improved iterators.

* Fri Apr  4 2003 Ville Skyttä <ville.skytta at iki.fi> - 0:2.52-2jpp
- Rebuilt for JPackage 1.5.

* Thu Mar 13 2003 Ville Skyttä <ville.skytta at iki.fi> - 2.52-1jpp
- Updated to 2.52.
- Comments from upstream (thanks to Sebastiano Vigna):
  - IMPORTANT: The package name has changed. Please remove manually
    the old package (if you have a RPM called "fastUtil" installed)
    and modify your sources.
  - Deleted docs from source tar.
  - Merged in patches to Makefile.

* Sun Mar  9 2003 Ville Skyttä <ville.skytta at iki.fi> - 2.51-2jpp
- First official JPackage release.
- Fix Group tags.
- No manual subpackage (there's no manual... :)

* Wed Feb 26 2003 Sebastiano Vigna <vigna at acm.org> 2.51
- New flexible trim(int) method..

* Tue Feb 18 2003 Sebastiano Vigna <vigna at acm.org> 2.50
- New linked hash tables and reference-based containers.

* Mon Dec 9 2002 Sebastiano Vigna <vigna at acm.org> 2.11
- JPackage compatible version
